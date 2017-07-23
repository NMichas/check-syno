package com.github.jitpack.synocheck.mib;

import com.github.jitpack.synocheck.mib.util.MibResult;
import com.github.jitpack.synocheck.util.OIDGetter;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NetworkInfo extends OIDGetter {

  public static final String ROOT_OID = ".1.3.6.1.2.1.31.1.1.1";

  public static List<MibResult> get(Snmp snmp, CommunityTarget communityTarget) throws
      IOException {
    List retVal = new ArrayList<>();

    /** Discover the number of NICs */
    for (int i = 1; i < 17; i++) {
      final String nicName = getSingleOID(snmp, communityTarget, ROOT_OID + ".1." + i);
      if (nicName.startsWith("Error") || nicName.startsWith("noSuchInstance")) {
        break;
      }

      String oReceived = getSingleOID(snmp, communityTarget, ROOT_OID + ".6." + i);
      String oTransmitted = getSingleOID(snmp, communityTarget, ROOT_OID + ".10." + i);

      retVal.add(new MibResult("NIC " + nicName + " - octets received", Long.parseLong(oReceived)));
      retVal.add(new MibResult("NIC " + nicName + " - octets transmitted", Long.parseLong(oTransmitted)));
    }

    return retVal;
  }

}

