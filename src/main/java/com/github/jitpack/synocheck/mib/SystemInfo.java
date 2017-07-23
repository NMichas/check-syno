package com.github.jitpack.synocheck.mib;

import com.github.jitpack.synocheck.mib.util.MibResult;
import com.github.jitpack.synocheck.util.OIDGetter;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SystemInfo extends OIDGetter {

  public static final String ROOT_OID = ".1.3.6.1.4.1.6574.1";

  public static List<MibResult> get(Snmp snmp, CommunityTarget communityTarget, long
      temperatureWarning, long temperatureCritical) throws
      IOException {
    List retVal = new ArrayList<>();

    retVal.add(new MibResult("System partition status",
        Long.parseLong(getSingleOID(snmp, communityTarget, ROOT_OID + ".1.0")),
        2l, 2l));

    retVal.add(new MibResult("System temperature",
        Long.parseLong(getSingleOID(snmp, communityTarget, ROOT_OID + ".2.0")),
        temperatureWarning, temperatureCritical));

    retVal.add(new MibResult("Power supply failure",
        Long.parseLong(getSingleOID(snmp, communityTarget, ROOT_OID + ".3.0")),
        2l, 2l));

    retVal.add(new MibResult("System fan failure",
        Long.parseLong(getSingleOID(snmp, communityTarget, ROOT_OID + ".4.1.0")),
        2l, 2l));

    retVal.add(new MibResult("CPU fan failure",
        Long.parseLong(getSingleOID(snmp, communityTarget, ROOT_OID + ".4.2.0")),
        2l, 2l));

    return retVal;
  }

}
