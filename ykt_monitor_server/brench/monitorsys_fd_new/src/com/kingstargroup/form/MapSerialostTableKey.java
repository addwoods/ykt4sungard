/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Fri Nov 04 15:15:58 CST 2005 by MyEclipse Hibernate Tool.
 */
package com.kingstargroup.form;

import java.io.Serializable;

/**
 * A class representing a composite primary key id for the MAP_SERIALOST_TABLE
 * table.  This object should only be instantiated for use with instances 
 * of the MapSerialostTable class.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized 
 * by MyEclipse Hibernate tool integration.
 */
public class MapSerialostTableKey
    implements Serializable
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private volatile int hashValue = 0;

    /** The value of the ANALYSISTIME component of this composite id. */
    private java.lang.String analysistime;

    /** The value of the SERIAL_NO component of this composite id. */
    private java.lang.Integer serialNo;

    /** The value of the DEVICE_ID component of this composite id. */
    private java.lang.String deviceId;

    /**
     * Simple constructor of MapSerialostTableKey instances.
     */
    public MapSerialostTableKey()
    {
    }

    /**
     * Returns the value of the analysistime property.
     * @return java.lang.String
     */
    public java.lang.String getAnalysistime()
    {
        return analysistime;
    }

    /**
     * Sets the value of the analysistime property.
     * @param analysistime
     */
    public void setAnalysistime(java.lang.String analysistime)
    {
        hashValue = 0;
        this.analysistime = analysistime;
    }

    /**
     * Returns the value of the serialNo property.
     * @return java.lang.Integer
     */
    public java.lang.Integer getSerialNo()
    {
        return serialNo;
    }

    /**
     * Sets the value of the serialNo property.
     * @param serialNo
     */
    public void setSerialNo(java.lang.Integer serialNo)
    {
        hashValue = 0;
        this.serialNo = serialNo;
    }

    /**
     * Returns the value of the deviceId property.
     * @return java.lang.String
     */
    public java.lang.String getDeviceId()
    {
        return deviceId;
    }

    /**
     * Sets the value of the deviceId property.
     * @param deviceId
     */
    public void setDeviceId(java.lang.String deviceId)
    {
        hashValue = 0;
        this.deviceId = deviceId;
    }

    /**
     * Implementation of the equals comparison on the basis of equality of the id components.
     * @param rhs
     * @return boolean
     */
    public boolean equals(Object rhs)
    {
        if (rhs == null)
            return false;
        if (! (rhs instanceof MapSerialostTableKey))
            return false;
        MapSerialostTableKey that = (MapSerialostTableKey) rhs;
        if (this.getAnalysistime() == null || that.getAnalysistime() == null)
        {
            return false;
        }
        if (! this.getAnalysistime().equals(that.getAnalysistime()))
        {
            return false;
        }
        if (this.getSerialNo() == null || that.getSerialNo() == null)
        {
            return false;
        }
        if (! this.getSerialNo().equals(that.getSerialNo()))
        {
            return false;
        }
        if (this.getDeviceId() == null || that.getDeviceId() == null)
        {
            return false;
        }
        if (! this.getDeviceId().equals(that.getDeviceId()))
        {
            return false;
        }
        return true;
    }

    /**
     * Implementation of the hashCode method conforming to the Bloch pattern with
     * the exception of array properties (these are very unlikely primary key types).
     * @return int
     */
    public int hashCode()
    {
        if (this.hashValue == 0)
        {
            int result = 17;
            int analysistimeValue = this.getAnalysistime() == null ? 0 : this.getAnalysistime().hashCode();
            result = result * 37 + analysistimeValue;
            int serialNoValue = this.getSerialNo() == null ? 0 : this.getSerialNo().hashCode();
            result = result * 37 + serialNoValue;
            int deviceIdValue = this.getDeviceId() == null ? 0 : this.getDeviceId().hashCode();
            result = result * 37 + deviceIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }
}