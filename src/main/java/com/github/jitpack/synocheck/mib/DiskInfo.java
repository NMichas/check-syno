package com.github.jitpack.synocheck.mib;

import com.github.jitpack.synocheck.mib.util.MibResult;
import com.github.jitpack.synocheck.util.OIDGetter;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DiskInfo {

  public static final String ROOT_OID = ".1.3.6.1.4.1.6574.2";

  public static List<MibResult> get(Snmp snmp, CommunityTarget communityTarget, Integer
      temperatureWarning, Integer temperatureCritical) throws
      IOException {
    List retVal = new ArrayList<>();

    /** Discover the number of disks */
    int maxDisks = 0;
    for (int i = 0; i < 64; i++) {
      final String singleOID = OIDGetter.getInstance().getSingleOID(snmp, communityTarget, ROOT_OID + ".1.1.2." + i);
      if (singleOID.startsWith("Error") || singleOID.startsWith("noSuchInstance")) {
        break;
      }
      maxDisks++;
    }

    for (int i = 0; i < maxDisks; i++) {
//      String diskName = OIDGetter.getInstance().getSingleOID(snmp, communityTarget, ROOT_OID + ".1.1.2." + i);
      String diskStatus = OIDGetter.getInstance().getSingleOID(snmp, communityTarget, ROOT_OID + ".1.1.5." + i);
      String diskTemp = OIDGetter.getInstance().getSingleOID(snmp, communityTarget, ROOT_OID + ".1.1.6." + i);
      retVal.add(new MibResult("Disk " + (i+1) + " - status", Long.parseLong(diskStatus), 4, 5));
      retVal.add(new MibResult("Disk " + (i+1) + " - temperature", Long.parseLong(diskTemp),
          temperatureWarning, temperatureCritical));
    }

    return retVal;
  }

}

