package com.github.jitpack.synocheck.mib;

import com.github.jitpack.synocheck.mib.util.MibResult;
import com.github.jitpack.synocheck.util.OIDGetter;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CPUInfo {

  public static List<MibResult> get(Snmp snmp, CommunityTarget communityTarget) throws
      IOException {
    List retVal = new ArrayList<>();

    retVal.add(new MibResult("CPU user time",
        Long.parseLong(OIDGetter.getInstance().getSingleOID(snmp, communityTarget, ".1.3.6.1.4.1.2021.11.9.0")), "%"));
    retVal.add(new MibResult("CPU system time",
        Long.parseLong(OIDGetter.getInstance().getSingleOID(snmp, communityTarget, ".1.3.6.1.4.1.2021.11.10.0")), "%"));
    retVal.add(new MibResult("CPU idle time",
        Long.parseLong(OIDGetter.getInstance().getSingleOID(snmp, communityTarget, ".1.3.6.1.4.1.2021.11.11.0")), "%"));
    retVal.add(new MibResult("CPU load 1m",
        Long.parseLong(OIDGetter.getInstance().getSingleOID(snmp, communityTarget, ".1.3.6.1.4.1.2021.10.1.5.1"))));
    retVal.add(new MibResult("CPU load 5m",
        Long.parseLong(OIDGetter.getInstance().getSingleOID(snmp, communityTarget, ".1.3.6.1.4.1.2021.10.1.5.2"))));
    retVal.add(new MibResult("CPU load 15m",
        Long.parseLong(OIDGetter.getInstance().getSingleOID(snmp, communityTarget, ".1.3.6.1.4.1.2021.10.1.5.3"))));

    return retVal;
  }

}
