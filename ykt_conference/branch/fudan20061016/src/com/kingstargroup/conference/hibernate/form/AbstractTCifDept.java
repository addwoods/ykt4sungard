/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Thu Mar 30 10:17:03 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.kingstargroup.conference.hibernate.form;

import java.io.Serializable;

/**
 * A class that represents a row in the T_CIF_DEPT table. 
 * You can customize the behavior of this class by editing the class, {@link TCifDept()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractTCifDept 
    implements Serializable
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.String deptCode;

    /** The value of the simple isAcademy property. */
    private java.lang.String isAcademy;

    /** The value of the simple deptName property. */
    private java.lang.String deptName;

    /** The value of the simple ename property. */
    private java.lang.String ename;

    /** The value of the simple parentdeptCode property. */
    private java.lang.String parentdeptCode;

    /** The value of the simple deptLevel property. */
    private java.lang.Integer deptLevel;

    /** The value of the simple isAvailability property. */
    private java.lang.String isAvailability;

    /** The value of the simple comments property. */
    private java.lang.String comments;

    /**
     * Simple constructor of AbstractTCifDept instances.
     */
    public AbstractTCifDept()
    {
    }

    /**
     * Constructor of AbstractTCifDept instances given a simple primary key.
     * @param deptCode
     */
    public AbstractTCifDept(java.lang.String deptCode)
    {
        this.setDeptCode(deptCode);
    }

    /**
     * Return the simple primary key value that identifies this object.
     * @return java.lang.String
     */
    public java.lang.String getDeptCode()
    {
        return deptCode;
    }

    /**
     * Set the simple primary key value that identifies this object.
     * @param deptCode
     */
    public void setDeptCode(java.lang.String deptCode)
    {
        this.hashValue = 0;
        this.deptCode = deptCode;
    }

    /**
     * Return the value of the IS_ACADEMY column.
     * @return java.lang.String
     */
    public java.lang.String getIsAcademy()
    {
        return this.isAcademy;
    }

    /**
     * Set the value of the IS_ACADEMY column.
     * @param isAcademy
     */
    public void setIsAcademy(java.lang.String isAcademy)
    {
        this.isAcademy = isAcademy;
    }

    /**
     * Return the value of the DEPT_NAME column.
     * @return java.lang.String
     */
    public java.lang.String getDeptName()
    {
        return this.deptName;
    }

    /**
     * Set the value of the DEPT_NAME column.
     * @param deptName
     */
    public void setDeptName(java.lang.String deptName)
    {
        this.deptName = deptName;
    }

    /**
     * Return the value of the E_NAME column.
     * @return java.lang.String
     */
    public java.lang.String getEname()
    {
        return this.ename;
    }

    /**
     * Set the value of the E_NAME column.
     * @param eName
     */
    public void setEname(java.lang.String ename)
    {
        this.ename = ename;
    }

    /**
     * Return the value of the PARENTDEPT_CODE column.
     * @return java.lang.String
     */
    public java.lang.String getParentdeptCode()
    {
        return this.parentdeptCode;
    }

    /**
     * Set the value of the PARENTDEPT_CODE column.
     * @param parentdeptCode
     */
    public void setParentdeptCode(java.lang.String parentdeptCode)
    {
        this.parentdeptCode = parentdeptCode;
    }

    /**
     * Return the value of the DEPT_LEVEL column.
     * @return java.lang.Integer
     */
    public java.lang.Integer getDeptLevel()
    {
        return this.deptLevel;
    }

    /**
     * Set the value of the DEPT_LEVEL column.
     * @param deptLevel
     */
    public void setDeptLevel(java.lang.Integer deptLevel)
    {
        this.deptLevel = deptLevel;
    }

    /**
     * Return the value of the IS_AVAILABILITY column.
     * @return java.lang.String
     */
    public java.lang.String getIsAvailability()
    {
        return this.isAvailability;
    }

    /**
     * Set the value of the IS_AVAILABILITY column.
     * @param isAvailability
     */
    public void setIsAvailability(java.lang.String isAvailability)
    {
        this.isAvailability = isAvailability;
    }

    /**
     * Return the value of the COMMENTS column.
     * @return java.lang.String
     */
    public java.lang.String getComments()
    {
        return this.comments;
    }

    /**
     * Set the value of the COMMENTS column.
     * @param comments
     */
    public void setComments(java.lang.String comments)
    {
        this.comments = comments;
    }

    /**
     * Implementation of the equals comparison on the basis of equality of the primary key values.
     * @param rhs
     * @return boolean
     */
    public boolean equals(Object rhs)
    {
        if (rhs == null)
            return false;
        if (! (rhs instanceof TCifDept))
            return false;
        TCifDept that = (TCifDept) rhs;
        if (this.getDeptCode() == null || that.getDeptCode() == null)
            return false;
        return (this.getDeptCode().equals(that.getDeptCode()));
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
            int deptCodeValue = this.getDeptCode() == null ? 0 : this.getDeptCode().hashCode();
            result = result * 37 + deptCodeValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }
}