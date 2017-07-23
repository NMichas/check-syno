package com.github.jitpack.synocheck.mib;

import com.github.jitpack.synocheck.mib.util.MibResult;
import com.github.jitpack.synocheck.util.OIDGetter;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StorageInfo extends OIDGetter {

  public static final String ROOT_OID = ".1.3.6.1.4.1.6574.101";

  public static List<MibResult> get(Snmp snmp, CommunityTarget communityTarget) throws
      IOException {
    List retVal = new ArrayList<>();

    /** Discover the number of disks */
    int maxDisks = 0;
    for (int i = 1; i < 65; i++) {
      final String singleOID = getSingleOID(snmp, communityTarget, ROOT_OID + ".1.1.1." + i);
      if (singleOID.startsWith("Error") || singleOID.startsWith("noSuchInstance")) {
        break;
      }
      maxDisks++;
    }

    for (int i = 1; i < maxDisks + 1; i++) {
      String bytesRead = getSingleOID(snmp, communityTarget, ROOT_OID + ".1.1.12." + i);
      String bytesWritten = getSingleOID(snmp, communityTarget, ROOT_OID + ".1.1.13." + i);
      String readAccesses = getSingleOID(snmp, communityTarget, ROOT_OID + ".1.1.5." + i);
      String writeAccesses = getSingleOID(snmp, communityTarget, ROOT_OID + ".1.1.6." + i);
      String loadPercentage = getSingleOID(snmp, communityTarget, ROOT_OID + ".1.1.8." + i);
      String loadPercentage1 = getSingleOID(snmp, communityTarget, ROOT_OID + ".1.1.9." + i);
      String loadPercentage5 = getSingleOID(snmp, communityTarget, ROOT_OID + ".1.1.10." + i);
      String loadPercentage15 = getSingleOID(snmp, communityTarget, ROOT_OID + ".1.1.11." + i);

      retVal.add(new MibResult("Disk " + i + " - bytes read", Long.parseLong
          (bytesRead), "B"));
      retVal.add(new MibResult("Disk " + i + " - bytes written", Long.parseLong
          (bytesWritten), "B"));
      retVal.add(new MibResult("Disk " + i + " - read access", Long.parseLong
          (readAccesses)));
      retVal.add(new MibResult("Disk " + i + " - write access", Long.parseLong
          (writeAccesses)));

      retVal.add(new MibResult("Disk " + i + " - load", Long.parseLong
          (loadPercentage), "%"));
      retVal.add(new MibResult("Disk " + i + " - load 1m", Long.parseLong
          (loadPercentage1), "%"));
      retVal.add(new MibResult("Disk " + i + " - load 5m", Long.parseLong
          (loadPercentage5), "%"));
      retVal.add(new MibResult("Disk " + i + " - load 15m", Long.parseLong
          (loadPercentage15), "%"));
    }

    return retVal;
  }

}

