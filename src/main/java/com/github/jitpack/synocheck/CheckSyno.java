package com.github.jitpack.synocheck;

import com.github.jitpack.synocheck.mib.CPUInfo;
import com.github.jitpack.synocheck.mib.DiskInfo;
import com.github.jitpack.synocheck.mib.FilesystemInfo;
import com.github.jitpack.synocheck.mib.MemoryInfo;
import com.github.jitpack.synocheck.mib.NetworkInfo;
import com.github.jitpack.synocheck.mib.RaidInfo;
import com.github.jitpack.synocheck.mib.StorageInfo;
import com.github.jitpack.synocheck.mib.SystemInfo;
import com.github.jitpack.synocheck.mib.util.MibResult;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CheckSyno {

  /**
   * JUL reference
   */
  private final static Logger LOGGER = Logger.getLogger(CheckSyno.class.getName());

  private String hostname;
  private String snmpCommunityName;
  private static Options cliOptions;

  static {
    /** Setup CLI options */
    cliOptions = new Options();
    cliOptions.addOption(
        Option.builder("h").longOpt("hostname").required().hasArg().numberOfArgs(1)
            .desc("The hostname of your Synology NAS.").build());
    cliOptions.addOption(
        Option.builder("c").longOpt("community").required(false).hasArg().numberOfArgs(1)
            .desc("The name of the SNMP community to use.").build());
    cliOptions.addOption(
        Option.builder("wt").longOpt("warningTemperature").required(false).hasArg().numberOfArgs(1)
            .desc("Warning threshold for temperature.").build());
    cliOptions.addOption(
        Option.builder("ct").longOpt("criticalTemperature").required(false).hasArg().numberOfArgs(1)
            .desc("Critical threshold for temperature.").build());
    cliOptions.addOption(
        Option.builder("wd").longOpt("warningDisk").required(false).hasArg().numberOfArgs(1)
            .desc("Warning disk usage percentage.").build());
    cliOptions.addOption(
        Option.builder("cd").longOpt("criticalDisk").required(false).hasArg().numberOfArgs(1)
            .desc("Critical disk usage percentage.").build());
  }

  public CheckSyno(String hostname, String snmpCommunityName) {
    this.hostname = hostname;
    this.snmpCommunityName = snmpCommunityName;
  }

  private List<MibResult> getMetrics() throws IOException {
    List retVal = new ArrayList<>();

    /** Establish a connection */
    Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
    snmp.listen();
    Address add = new UdpAddress(hostname + "/" + "161");
    CommunityTarget communityTarget = new CommunityTarget();
    communityTarget.setAddress(add);
    communityTarget.setTimeout(1000);
    communityTarget.setRetries(3);
    communityTarget.setCommunity(new OctetString(snmpCommunityName));
    communityTarget.setVersion(SnmpConstants.version2c);

    /** Get info */
    retVal.addAll(SystemInfo.get(snmp, communityTarget, 45, 50));
    retVal.addAll(DiskInfo.get(snmp, communityTarget, 45, 50));
    retVal.addAll(RaidInfo.get(snmp, communityTarget));
    retVal.addAll(StorageInfo.get(snmp, communityTarget));
    retVal.addAll(CPUInfo.get(snmp, communityTarget));
    retVal.addAll(MemoryInfo.get(snmp, communityTarget));
    retVal.addAll(NetworkInfo.get(snmp, communityTarget));
    retVal.addAll(FilesystemInfo.get(snmp, communityTarget, 80, 90));

    /** Close connection */
    snmp.close();

    /** Return value */
    return retVal;
  }

  public void processMetricsAndExit(List<MibResult> metrics) {
    StringBuffer retVal = new StringBuffer();
    int status = 0;
    for (int i = 0; i < metrics.size(); i++) {
      MibResult metric = metrics.get(i);
      status =
          metric.getWarning() != null ? (metric.getValue() > metric.getWarning() ? 1 : 0) : status;
      status = metric.getCritical() != null ? (metric.getValue() > metric.getCritical() ? 2 : 0)
          : status;
      retVal.append(metric.toString());
      if (i < metrics.size() - 1) {
        retVal.append(", ");
      }
    }

    switch (status) {
      case 1:
        System.out.println("WARNING | " + retVal.toString());
        System.exit(1);
      case 2:
        System.out.println("CRITICAL | " + retVal.toString());
        System.exit(2);
      default:
        System.out.println("OK | " + retVal.toString());
        System.exit(0);
    }
  }


  public static void main(String[] args) throws Exception {
    /** Show help if no arguments passed */
    if (args.length == 0) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("help", cliOptions);
      System.exit(3);
    }

    /** Parse cli and create App instance */
    CommandLine cmd = new DefaultParser().parse(cliOptions, args);

    CheckSyno checkSyno = new CheckSyno(cmd.getOptionValue("h"), cmd.hasOption("c") ? cmd
        .getOptionValue("c") : "public");
    final List<MibResult> metrics = checkSyno.getMetrics();
    checkSyno.processMetricsAndExit(metrics);
  }

}