package org.michael.kmp.playground.core.network

interface UseCase<in Params, out Result> {
    suspend operator fun invoke(params: Params): NetworkResult<Result>
}