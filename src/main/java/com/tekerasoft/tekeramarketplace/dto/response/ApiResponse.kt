package com.tekerasoft.tekeramarketplace.dto.response

class ApiResponse<T> @JvmOverloads constructor(var message: String,var statusCode:Int, var data: T? = null)
