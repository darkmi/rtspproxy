/***************************************************************************
 * * This program is free software; you can redistribute it and/or modify * it under the terms of
 * the GNU General Public License as published by * the Free Software Foundation; either version 2
 * of the License, or * (at your option) any later version. * * Copyright (C) 2005 - Matteo Merli -
 * matteo.merli@gmail.com * *
 ***************************************************************************/

package rtspproxy;

/**
 * @author Matteo Merli
 */
public interface ProxyService {

  /**
   * Starts the service.
   * 
   * @throws Exception
   */
  public void start() throws Exception;

  /**
   * Stops the service
   * 
   * @throws Exception
   */
  public void stop() throws Exception;

  /**
   * @return true if the service is started, false otherwise.
   */
  // TODO: Not really sure if it's needed
  // public boolean isRunning();

}
