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

import org.bedework.indexer.crawler.Crawl;
import org.bedework.indexer.crawler.Crawl.CrawlResult;
import org.bedework.sysevents.NotificationException;
import org.bedework.sysevents.events.SysEvent;
import org.bedework.sysevents.listeners.JmsSysEventListener;

import edu.rpi.sss.util.Args;

import java.util.ArrayList;
import java.util.Collection;

/** The crawler program for the bedework calendar system.
 *
 * @author douglm
 *
 */
public class BwIndexApp extends JmsSysEventListener {
  protected String account;

  protected Collection<String> skipPaths = new ArrayList<String>();

  private String indexBuildLocationPrefix;

  private boolean discardMessages;

  private boolean doCrawl;

  private boolean doListen;

  private int publicThreads = 10;

  private int userThreads = 10;

  private long messageCount;

  private MessageProcessor msgProc;

  BwIndexApp() {
  }

  /**
   * @param val
   */
  public void setAccount(final String val) {
    account = val;
  }

  /**
   * @return String account we use
   */
  public String getAccount() {
    return account;
  }

  /**
   * @param val
   */
  public void setDiscardMessages(final boolean val) {
    discardMessages = val;
  }

  /**
   * @return true if we just discard messages
   */
  public boolean getDiscardMessages() {
    return discardMessages;
  }

  /**
   * @return number of messages processed
   */
  public long getMessageCount() {
    return messageCount;
  }

  /**
   * @param val
   */
  public void setIndexBuildLocationPrefix(final String val) {
    indexBuildLocationPrefix = val;
  }

  CrawlResult crawl() throws Throwable {
    if (account == null) {
      account = System.getProperty("user.name");
    }

    Crawl crawler = new Crawl(account, // admin account
                              indexBuildLocationPrefix,
                              skipPaths,
                              publicThreads,
                              userThreads);

    CrawlResult cr = crawler.crawl();

    checkThreads("public", crawler.getPublicThreads());
    checkThreads("user", crawler.getUserThreads());

    return cr;
  }

  void checkThreads(final String name, final ThreadGroup tg) {
    int active = tg.activeCount();

    if (active == 0) {
      return;
    }

    error("Still " + active + " active " + name + " threads");

    Thread[] activeThreads = new Thread[active];

    int ret = tg.enumerate(activeThreads);

    for (int i = 0; i < ret; i++) {
      error("Thread " + activeThreads[i].getName() +
            " is still active");
    }
  }

  void listen() throws Throwable {
    open(crawlerQueueName);

    msgProc = new MessageProcessor(account, // admin account
                                   skipPaths,
                                   publicThreads,
                                   userThreads);

    process(false);
  }

  /* (non-Javadoc)
   * @see org.bedework.sysevents.listeners.JmsSysEventListener#action(org.bedework.sysevents.events.SysEvent)
   */
  @Override
  public void action(final SysEvent ev) throws NotificationException {
    if (ev == null) {
      return;
    }

    try {
      messageCount++;

      if (discardMessages) {
        return;
      }

      msgProc.processMessage(ev);
    } catch (Throwable t) {
      throw new NotificationException(t);
    }
  }

  boolean processArgs(final Args args) throws Throwable {
    if (args == null) {
      return true;
    }

    while (args.more()) {
      if (args.ifMatch("")) {
        continue;
      }

      if (args.ifMatch("-user")) {
        account = args.next();
      } else if (args.ifMatch("-appname")) {
        args.next(); // Not used at the moment
      } else if (args.ifMatch("-crawl")) {
        doCrawl = true;
      } else if (args.ifMatch("-indexlocprefix")) {
        indexBuildLocationPrefix = args.next();
      } else if (args.ifMatch("-listen")) {
        doListen = true;
      } else if (args.ifMatch("-skip")) {
        skipPaths.add(args.next());
      } else {
        error("Illegal argument: " + args.current());
        usage();
        return false;
      }
    }

    return true;
  }

  void usage() {
    System.out.println("Usage:");
    System.out.println("args   -user <admin-account>");
    System.out.println("       -crawl ");
    System.out.println("       -indexlocprefix <prefix> ");
    System.out.println("           prefix to apply to system index root ");
    System.out.println("       -listen ");
    System.out.println("       -skip <path-to-skip>");
    System.out.println("");
  }

  /**
   * @param args
   */
  public static void main(final String[] args) {
    try {
      BwIndexApp capp = new BwIndexApp();

      if (!capp.processArgs(new Args(args))) {
        return;
      }

      if (capp.doCrawl) {
        capp.info("Start crawl of data");

        CrawlResult cr = capp.crawl();
        if (!cr.ok) {
          System.exit(4);
        }

        capp.info("End crawl of data");
      }

      if (capp.doListen) {
        capp.info("Start listening");

        capp.listen();

        capp.info("Shut down listener");
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }

    System.exit(0);
  }
}
