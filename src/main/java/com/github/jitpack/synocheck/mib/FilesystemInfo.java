package com.github.jitpack.synocheck.mib;

import com.github.jitpack.synocheck.mib.util.MibResult;
import com.github.jitpack.synocheck.util.OIDGetter;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilesystemInfo extends OIDGetter {

  public static final String ROOT_OID = "1.3.6.1.2.1.25.2.3.1";

  public static List<MibResult> get(Snmp snmp, CommunityTarget communityTarget, int fullWarning,
      int fullCritical) throws
      IOException {
    List retVal = new ArrayList<>();

    /** Discover the filesystems */
    for (int i = 1; i < 256; i++) {
      final String fsName = getSingleOID(snmp, communityTarget, ROOT_OID + ".3." + i);
      if (fsName.startsWith("/volume")) {
        long allocationUnits = Long.parseLong(getSingleOID(snmp, communityTarget, ROOT_OID +
            ".4." + i));
        long size = Long.parseLong(getSingleOID(snmp, communityTarget, ROOT_OID + ".5." + i));
        long used = Long.parseLong(getSingleOID(snmp, communityTarget, ROOT_OID + ".6." + i));
        long bytesRead = Long.parseLong(getSingleOID(snmp, communityTarget,
            ".1.3.6.1.4.1.2021.13.15.1.1.12." + i));
        long bytesWritten = Long.parseLong(getSingleOID(snmp, communityTarget,
            ".1.3.6.1.4.1.2021.13.15.1.1.13." + i));

        int totalSizeInGB = (int)((allocationUnits * size) / 1000000000);
        int totalUsedInGB = (int)((used * allocationUnits) / 1000000000);
        int percentageUsed = totalUsedInGB * 100 / totalSizeInGB;

        retVal.add(new MibResult("Filesystem " + fsName + " - used (%)",
            (long)percentageUsed, (long)fullWarning, (long)fullCritical));
        retVal.add(new MibResult("Filesystem " + fsName + " - total (in GB)",
            (long)totalSizeInGB));
        retVal.add(new MibResult("Filesystem " + fsName + " - used (in GB)",
            (long)totalUsedInGB));
        retVal.add(new MibResult("Filesystem " + fsName + " - remaining (in GB)",
            (long)totalSizeInGB - (long)totalUsedInGB));
        retVal.add(new MibResult("Filesystem " + fsName + " - bytes read",
            bytesRead));
        retVal.add(new MibResult("Filesystem " + fsName + " - bytes written",
            bytesWritten));
      }
    }

    return retVal;
  }

}

