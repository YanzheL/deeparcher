package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.parsing

import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import org.springframework.batch.item.file.mapping.FieldSetMapper


interface PDNSLogFieldSetMapper : FieldSetMapper<PDnsData>