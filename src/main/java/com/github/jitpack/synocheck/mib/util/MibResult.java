package com.github.jitpack.synocheck.mib.util;

public class MibResult {
  private String oid;
  private String friendlyName;
  /**
   * 0: OK
   * 1: Warning
   * 2: Critical
   */
  private long value;
  private Long warning;
  private Long critical;

  public MibResult(String friendlyName, Long value, Long warning,
      Long critical) {
    this.friendlyName = friendlyName;
    this.value = value;
    this.warning = warning;
    this.critical = critical;
  }

  public MibResult(String friendlyName, Long value) {
    this.friendlyName = friendlyName;
    this.value = value;
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
    String retVal = friendlyName + "=" + value ;

    if (warning != null) {
      retVal += ";" + warning;
    }

    if (critical != null) {
      retVal += ";" + critical;
    }

    return retVal;

  }
}
