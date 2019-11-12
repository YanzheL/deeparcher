package com.hitnslab.dnssecurity.pdnsdataloader.io

import com.hitnslab.dnssecurity.pdnsdataloader.model.PDnsDataDAO
import org.springframework.batch.item.database.ItemPreparedStatementSetter
import org.springframework.stereotype.Component
import java.sql.PreparedStatement
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Component
class PDNSPreparedStatementSetter : ItemPreparedStatementSetter<PDnsDataDAO> {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.nnn")

    /**
     * ( client_ip, domain, q_time, q_type, r_cnames, r_code, r_ips, top_priv_domain )
     */
    override fun setValues(item: PDnsDataDAO, ps: PreparedStatement) {
        ps.setInt(1, item.clientIp)
        ps.setString(2, item.domain)
        ps.setString(3, item.queryTime.atZone(ZoneId.systemDefault()).format(dateTimeFormatter))
        ps.setInt(4, item.queryType.value)
        ps.setString(5, "{${item.cnames.joinToString(",")}}")
        ps.setInt(6, item.replyCode.value)
        ps.setString(7, "{${item.ips.joinToString(",")}}")
        ps.setString(8, item.topPrivateDomain)
    }
}