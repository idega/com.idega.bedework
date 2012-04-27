/* ********************************************************************
    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License. You may obtain a
    copy of the License at:
        
    http://www.apache.org/licenses/LICENSE-2.0
        
    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied. See the License for the
    specific language governing permissions and limitations
    under the License.
*/
package org.bedework.indexer;

import org.bedework.indexer.BwIndexerMBean;
import org.bedework.indexer.crawler.Crawl.CrawlResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author douglm
 *
 */
public class BwIndexer extends BwIndexApp implements BwIndexerMBean {
  private boolean running;

  private class ProcessorThread extends Thread {
    boolean showedTrace;

    /**
     * @param name - for the thread
     */
    public ProcessorThread(final String name) {
      super(name);
    }
    @Override
    public void run() {
      while (running) {
        try {
          listen();
        } catch (InterruptedException ie) {
          break;
        } catch (Throwable t) {
          if (!showedTrace) {
            error(t);
//            showedTrace = true;
          } else {
            error(t.getMessage());
          }
        } finally {
          close();
        }
      }
    }
  }

  private ProcessorThread processor;

  public String getName() {
    /* This apparently must be the same as the name attribute in the
     * jboss service definition
     */
    return "org.bedework:service=Indexer";
  }

  /* an example say's we need this  - we'll see
  public MBeanInfo getMBeanInfo() throws Exception {
    InitialContext ic = new InitialContext();
    RMIAdaptor server = (RMIAdaptor) ic.lookup("jmx/rmi/RMIAdaptor");

    ObjectName name = new ObjectName(MBEAN_OBJ_NAME);

    // Get the MBeanInfo for this MBean
    MBeanInfo info = server.getMBeanInfo(name);
    return info;
  }
  */

  /* (non-Javadoc)
   * @see org.bedework.indexer.BwIndexerMBean#rebuildIndex()
   */
  public List<String> rebuildIndex() {
    try {
      setIndexBuildLocationPrefix(System.getProperty("org.bedework.data.dir"));

      CrawlResult cr = crawl();

      return cr.infoLines;
    } catch (Throwable t) {
      List<String> infoLines = new ArrayList<String>();

      infoLines.add("***********************************\n");
      infoLines.add("Error rebuilding indexes.\n");
      infoLines.add("***********************************\n");
      error("Error rebuilding indexes.");
      error(t);

      return infoLines;
    }
  }

  /* (non-Javadoc)
   * @see org.bedework.indexer.BwIndexerMBean#isStarted()
   */
  public boolean isStarted() {
    return (processor != null) && processor.isAlive();
  }

  /* (non-Javadoc)
   * @see org.bedework.indexer.BwIndexerMBean#start()
   */
  public synchronized void start() {
    if (processor != null) {
      error("Already started");
      return;
    }

    running = true;

    info("************************************************************");
    info(" * Starting indexer");
    info("************************************************************");

    processor = new ProcessorThread(getName());
    processor.start();
  }

  /* (non-Javadoc)
   * @see org.bedework.indexer.BwIndexerMBean#stop()
   */
  public synchronized void stop() {
    if (processor == null) {
      error("Already stopped");
      return;
    }

    info("************************************************************");
    info(" * Stopping indexer");
    info("************************************************************");

    running = false;

    processor.interrupt();
    try {
      processor.join(20 * 1000);
    } catch (InterruptedException ie) {
    } catch (Throwable t) {
      error("Error waiting for processor termination");
      error(t);
    }

    processor = null;

    info("************************************************************");
    info(" * Indexer terminated");
    info("************************************************************");
  }

  /* (non-Javadoc)
   * @see org.bedework.indexer.BwIndexerMBean#setSkipPaths(java.lang.String)
   */
  public void setSkipPaths(final String val) {
    String[] paths = val.split(":");

    skipPaths.clear();

    for (String path:paths) {
      skipPaths.add(path);
    }
  }

  /* (non-Javadoc)
   * @see org.bedework.indexer.BwIndexerMBean#getSkipPaths()
   */
  public String getSkipPaths() {
    String delim = "";
    StringBuilder sb = new StringBuilder();

    for (String s: skipPaths) {
      sb.append(delim);
      sb.append(s);

      delim = ":";
    }

    return sb.toString();
  }
}
