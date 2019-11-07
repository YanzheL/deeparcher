package com.hitnslab.dnssecurity.pdnsdataloader.parsing

import com.hitnslab.dnssecurity.pdnsdataloader.model.PDNSInfo
import org.springframework.batch.item.file.mapping.FieldSetMapper

interface PDNSLogFieldSetMapper : FieldSetMapper<PDNSInfo> {
}