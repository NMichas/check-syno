package com.github.jitpack.synocheck.mib.util;

public class MibResult {
  private String oid;
  private String friendlyName;
  private long value;
  private Long warning;
  private Long critical;
  private String unit;

  public MibResult(String friendlyName, Long value, Long warning,
      Long critical, String unit) {
    this.friendlyName = friendlyName;
    this.value = value;
    this.warning = warning;
    this.critical = critical;
    this.unit = unit;
  }

  public MibResult(String friendlyName, Long value, Long warning,
      Long critical) {
    this.friendlyName = friendlyName;
    this.value = value;
    this.warning = warning;
    this.critical = critical;
  }

  public MibResult(String friendlyName, Long value, String unit) {
    this.friendlyName = friendlyName;
    this.value = value;
    this.unit = unit;
  }

  public MibResult(String friendlyName, Long value) {
    this.friendlyName = friendlyName;
    this.value = value;
  }

  public String getOid() {
    return oid;
  }

  public void setOid(String oid) {
    this.oid = oid;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getFriendlyName() {
    return friendlyName;
  }

  public void setFriendlyName(String friendlyName) {
    this.friendlyName = friendlyName;
  }

  public long getValue() {
    return value;
  }

  public void setValue(long value) {
    this.value = value;
  }

  public Long getWarning() {
    return warning;
  }

  public void setWarning(Long warning) {
    this.warning = warning;
  }

  public Long getCritical() {
    return critical;
  }

  public void setCritical(Long critical) {
    this.critical = critical;
  }

  @Override
  public String toString() {

    String retVal = "'" + friendlyName.replaceAll("'", "''") + "'=" + value;
    retVal += unit != null ? unit : "";

    retVal += ";" + (warning != null ? warning : "");
    retVal += ";" + (critical != null ? critical : "");
    //retVal += ";;";

    return retVal;

  }
}
