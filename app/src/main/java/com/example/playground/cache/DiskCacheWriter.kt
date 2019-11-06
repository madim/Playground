package com.example.playground.cache

import java.io.File

class DataCacheWriter<DataType>(
    private val encoder: Encoder<DataType>,
    private val data: DataType
) : DiskCache.Writer {

    override fun write(file: File): Boolean {
        return encoder.encode(data, file)
    }
}