package uk.ac.york.its.vle.b2.dsk.model;

import blackboard.data.AbstractIdentifiable;
import blackboard.persist.DataType;
import blackboard.persist.Id;
import blackboard.persist.impl.mapping.annotation.Column;
import blackboard.persist.impl.mapping.annotation.PrimaryKey;
import blackboard.persist.impl.mapping.annotation.RefersTo;
import blackboard.persist.impl.mapping.annotation.Table;

import java.util.Date;

@Table("york_dsk_change_message")
public class ChangeMessage extends AbstractIdentifiable {
    public static final DataType DATA_TYPE = new DataType(ChangeMessage.class);

    public DataType getDataType() {
        return DATA_TYPE;
    }

    @PrimaryKey
    private int pk1;

    @Column(value = "dsk_type")
    private String dskType;

    @Column(value = "user_id")
    private String userId;

    @Column(value = "server_host")
    private String serverHost;

    @Column(value = "server_ip")
    private String serverIP;

    @Column(value = "client_host")
    private String clientHost;

    @Column(value = "client_ip")
    private String clientIP;

    @Column(value = "data_set_uid")
    private String dataSetUid;


//    @Column(value = "dtcreated")
    private Date dateCreated;

    @Column(value = "change_comment")
    private String changeComment;


    public String getDskType() {
        return dskType;
    }

    public void setDskType(String dskType) {
        this.dskType = dskType;
    }

    public int getPk1() {
        return pk1;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getClientHost() {
        return clientHost;
    }

    public void setClientHost(String clientHost) {
        this.clientHost = clientHost;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public String getDataSetUid() {
        return dataSetUid;
    }

    public void setDataSetUid(String dataSetUid) {
        this.dataSetUid = dataSetUid;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getChangeComment() {
        return changeComment;
    }

    public void setChangeComment(String changeComment) {
        this.changeComment = changeComment;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("\n");
        sb.append("--------------------------------------------").append("\n");
        sb.append("dskType: " + dskType).append("\n");
        sb.append("userId: " + userId).append("\n");
        sb.append("serverHost: " + clientIP).append("\n");
        sb.append("serverIP: " + clientIP).append("\n");
        sb.append("clientHost: " + clientIP).append("\n");
        sb.append("clientIP: " + clientIP).append("\n");
        sb.append("dataSetUid: " + dataSetUid).append("\n");
        if (null != dateCreated) {
            sb.append("dtCreadted: " + dateCreated.toString()).append("\n");
        } else {
            sb.append("dtCreadted: " + null).append("\n");
        }
        sb.append("changeComment: " + changeComment).append("\n");
        sb.append("--------------------------------------------").append("\n");
        return sb.toString();
    }
}