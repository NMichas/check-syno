package com.github.jitpack.synocheck.mib;

import com.github.jitpack.synocheck.mib.util.MibResult;
import com.github.jitpack.synocheck.util.OIDGetter;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RaidInfo {

  public static final String ROOT_OID = ".1.3.6.1.4.1.6574.3";

  public static List<MibResult> get(Snmp snmp, CommunityTarget communityTarget) throws
      IOException {
    List retVal = new ArrayList<>();

    /** Discover the number of volumes */
    int maxVolumes = 0;
    for (int i = 0; i < 64; i++) {
      final String singleOID = OIDGetter.getInstance().getSingleOID(snmp, communityTarget, ROOT_OID + ".1.1.2." + i);
      if (singleOID.startsWith("Error") || singleOID.startsWith("noSuchInstance")) {
        break;
      }
      maxVolumes++;
    }

    for (int i = 0; i < maxVolumes; i++) {
      String volumeName = OIDGetter.getInstance().getSingleOID(snmp, communityTarget, ROOT_OID + ".1.1.2." + i);
      String volumeStatus = OIDGetter.getInstance().getSingleOID(snmp, communityTarget, ROOT_OID + ".1.1.3." + i);
      retVal.add(new MibResult(volumeName + " - status", Long.parseLong(volumeStatus), 2, 11));
    }

    return retVal;
  }

}

