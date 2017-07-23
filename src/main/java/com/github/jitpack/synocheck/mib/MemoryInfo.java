package com.github.jitpack.synocheck.mib;

import com.github.jitpack.synocheck.mib.util.MibResult;
import com.github.jitpack.synocheck.util.OIDGetter;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MemoryInfo extends OIDGetter {

  public static List<MibResult> get(Snmp snmp, CommunityTarget communityTarget) throws
      IOException {
    List retVal = new ArrayList<>();

    retVal.add(new MibResult("Memory - total swap space",
        Long.parseLong(getSingleOID(snmp, communityTarget, ".1.3.6.1.4.1.2021.4.3.0")), "KB"));
    retVal.add(new MibResult("Memory - swap space unused",
        Long.parseLong(getSingleOID(snmp, communityTarget, ".1.3.6.1.4.1.2021.4.4.0")), "KB"));
    retVal.add(new MibResult("Memory - physical total",
        Long.parseLong(getSingleOID(snmp, communityTarget, ".1.3.6.1.4.1.2021.4.5.0")), "KB"));
    retVal.add(new MibResult("Memory - physical unused",
        Long.parseLong(getSingleOID(snmp, communityTarget, ".1.3.6.1.4.1.2021.4.6.0")), "KB"));
    retVal.add(new MibResult("Memory - free",
        Long.parseLong(getSingleOID(snmp, communityTarget, ".1.3.6.1.4.1.2021.4.11.0")), "KB"));
    retVal.add(new MibResult("Memory - allocated",
        Long.parseLong(getSingleOID(snmp, communityTarget, ".1.3.6.1.4.1.2021.4.13.0")), "KB"));

    return retVal;
  }

}
