package com.brunoponte.everythinglisboa.domain

data class RepoResult(val items: List<RepoItem>)

data class RepoItem(
    val id: Long?,
    val name: String?,
    val fullName: String?,
    val owner: RepoOwner,
    val private: Boolean,
    val htmlUrl: String?,
    val description: String?)

data class RepoOwner(val login: String?, val id: Long?, val avatarUrl: String?)