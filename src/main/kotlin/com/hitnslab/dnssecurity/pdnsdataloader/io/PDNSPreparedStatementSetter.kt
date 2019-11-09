package com.hitnslab.dnssecurity.pdnsdataloader.io

import com.hitnslab.dnssecurity.pdnsdataloader.model.PDnsDataDAO
import org.springframework.batch.item.database.ItemPreparedStatementSetter
import org.springframework.stereotype.Component
import java.sql.PreparedStatement
import java.text.SimpleDateFormat

@Component
class PDNSPreparedStatementSetter : ItemPreparedStatementSetter<PDnsDataDAO> {

    /**
     * ( client_ip, domain, q_time, q_type, r_cnames, r_code, r_ips, top_priv_domain )
     */
    override fun setValues(item: PDnsDataDAO, ps: PreparedStatement) {
        ps.setInt(1, item.clientIp)
        ps.setString(2, item.domain)
        ps.setString(3, SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").format(item.queryTime))
        ps.setString(4, item.queryType)
        ps.setString(5, "{${item.cnames.joinToString(",")}}")
        ps.setString(6, item.replyCode)
        ps.setString(7, "{${item.ips.joinToString(",")}}")
        ps.setString(8, item.topPrivateDomain)
    }
}