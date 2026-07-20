package com.juliohaga.kelp.core.command

sealed class ExecutionResult {
    data object Success : ExecutionResult()
    data object NoPermission : ExecutionResult()
    data object SubCommandNotFound : ExecutionResult()
    data class RequirementDenied(val message: String) : ExecutionResult()
    data class ExecutionError(val cause: Throwable) : ExecutionResult()
}