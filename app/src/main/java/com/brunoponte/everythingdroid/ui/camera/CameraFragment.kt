package com.brunoponte.everythingdroid.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.brunoponte.everythingdroid.databinding.FragmentCameraBinding
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.exceptions.NotYetAvailableException
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
import java.nio.ByteBuffer


class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private var imagePreview: Preview? = null

    private val viewModel: CameraViewModel by viewModels()

    private var userRequestedInstall = true
    private var arCoreSession: Session? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        if (!allPermissionsGranted()) {
            requestPermissions(
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
            return
        }

        if (!requestArCoreInstallation()) {
            return
        }

        startCamera()
        startCapturingDepth()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Release native heap memory used by an ARCore session.
        arCoreSession?.close();
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        cameraProviderFuture.addListener({
            imagePreview = Preview.Builder().apply {
                setTargetAspectRatio(AspectRatio.RATIO_16_9)
                setTargetRotation(binding.previewView.display.rotation)
            }.build()

            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.bindToLifecycle(this, cameraSelector, imagePreview)
            binding.previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            imagePreview?.setSurfaceProvider(binding.previewView.surfaceProvider)
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun startCapturingDepth() {
        arCoreSession?.let {
            // Retrieve the depth image for the current frame, if available.
            try {
                val frame = it.update()
                frame.acquireDepthImage16Bits().use { depthImage ->
                    // Use the depth image here.
                    val buffer = depthImage.planes[0].buffer
                    val bytes = ByteArray(buffer.remaining())
                    buffer.get(bytes)
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
                    binding.depthImage.setImageBitmap(bitmap)
                }
            } catch (e: NotYetAvailableException) {
                // This means that depth data is not available yet.
                // Depth data will not be available if there are no tracked
                // feature points. This can happen when there is no motion, or when the
                // camera loses its ability to track objects in the surrounding
                // environment.
            }

        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    // Verify that ARCore is installed and using the current version.
    private fun requestArCoreInstallation(): Boolean {
        // Ensure that Google Play Services for AR and ARCore device profile data are
        // installed and up to date.
        try {
            if (arCoreSession == null) {
                when (ArCoreApk.getInstance().requestInstall(activity, userRequestedInstall)) {
                    ArCoreApk.InstallStatus.INSTALLED -> {
                        createArCoreSession()
                        return true
                    }
                    ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                        // When this method returns `INSTALL_REQUESTED`:
                        // 1. ARCore pauses this activity.
                        // 2. ARCore prompts the user to install or update Google Play
                        //    Services for AR (market://details?id=com.google.ar.core).
                        // 3. ARCore downloads the latest device profile data.
                        // 4. ARCore resumes this activity. The next invocation of
                        //    requestInstall() will either return `INSTALLED` or throw an
                        //    exception if the installation or update did not succeed.
                        userRequestedInstall = false
                    }
                }
            }
        } catch (e: UnavailableUserDeclinedInstallationException) {
            // User declined installation. Go back
            Toast.makeText(activity, "AR Core must be installed", Toast.LENGTH_LONG)
                .show()
            findNavController().popBackStack()
        } catch (e: Exception) {
            // Unexpected error.
            Toast.makeText(activity, "An error occurred", Toast.LENGTH_LONG)
                .show()
            findNavController().popBackStack()
        }

        return false
    }

    private fun createArCoreSession() {
        // Success: Safe to create the AR session.
        arCoreSession = Session(activity).apply {
            // Create a session config.
            val config = Config(this)

            // Do feature-specific operations here, such as enabling depth or turning on
            // support for Augmented Faces.
            // Check whether the user's device supports the Depth API.
            val isDepthSupported = this.isDepthModeSupported(Config.DepthMode.AUTOMATIC)
            if (isDepthSupported) {
                config.depthMode = Config.DepthMode.AUTOMATIC
            }

            // Configure the session.
            this.configure(config)
            this.resume()
        }
    }

    companion object {

        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    }
}