package com.hitnslab.dnssecurity.deeparcher.whitelistfilter

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "app")
@Component
class AppProperties {

    var whitelist = Whitelist()

    var input = Input()

    var output = Output()

    class Whitelist {
        var file: String = "whitelist.txt"
    }

    class Input {
        var topic: String = "whitelist.txt"
    }

    class Output {
        var file: String = "whitelist.txt"
    }
}