package com.tekerasoft.tekeramarketplace.dto.response

class ApiResponse<T>(var message: String, var data: T, var success: Boolean)
