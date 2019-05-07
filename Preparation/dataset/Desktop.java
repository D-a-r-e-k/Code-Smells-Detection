package org.lnicholls.galleon.apps.desktop;

/*
 * Copyright (C) 2005 Leon Nicholls
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * 
 * See the file "COPYING" for more details.
 */

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.awt.peer.RobotPeer;

import org.apache.log4j.Logger;
import org.lnicholls.galleon.app.AppFactory;
import org.lnicholls.galleon.media.ImageManipulator;
import org.lnicholls.galleon.util.Tools;
import org.lnicholls.galleon.widget.DefaultApplication;
import org.lnicholls.galleon.widget.DefaultScreen;

import sun.awt.ComponentFactory;

import com.tivo.hme.interfaces.IContext;

public class Desktop extends DefaultApplication {

	private static Logger log = Logger.getLogger(Desktop.class.getName());

	public final static String TITLE = "Desktop";

	public void init(IContext context) throws Exception {
		super.init(context);

		push(new DesktopScreen(this), TRANSITION_NONE);

		initialize();
	}

	public class DesktopScreen extends DefaultScreen {

		public DesktopScreen(Desktop app) {
			super(app, null, false);
		}

		private void update() {
			BufferedImage image = null;
			try {
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				Toolkit nativeToolkit = Tools.getDefaultToolkit();
				if (toolkit != nativeToolkit) {
					GraphicsDevice screenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment()
							.getDefaultScreenDevice();

					Robot robot = new Robot(screenDevice);
					RobotPeer peer = ((ComponentFactory) nativeToolkit).createRobot(robot, screenDevice);

					Dimension screenSize = nativeToolkit.getScreenSize();
					Rectangle screenRect = new Rectangle(screenSize);

					DataBufferInt buffer;
					WritableRaster raster;
					DirectColorModel screenCapCM = screenCapCM = new DirectColorModel(24,
					/* red mask */0x00FF0000,
					/* green mask */0x0000FF00,
					/* blue mask */0x000000FF);

					int pixels[];
					int[] bandmasks = new int[3];

					pixels = peer.getRGBPixels(screenRect);
					buffer = new DataBufferInt(pixels, pixels.length);

					bandmasks[0] = screenCapCM.getRedMask();
					bandmasks[1] = screenCapCM.getGreenMask();
					bandmasks[2] = screenCapCM.getBlueMask();

					raster = Raster.createPackedRaster(buffer, screenRect.width, screenRect.height, screenRect.width,
							bandmasks, null);

					image = new BufferedImage(screenCapCM, raster, false, null);
				} else {
					Dimension screenSize = toolkit.getScreenSize();
					Rectangle screenRect = new Rectangle(screenSize);
					// create screen shot
					Robot robot = new Robot();
					image = robot.createScreenCapture(screenRect);
				}
			} catch (Exception ex) {
				Tools.logException(Desktop.class, ex);
			}

			try {
				setPainting(false);
				if (image != null && getApp().getContext() != null) {
					if (image.getWidth() > 640 || image.getHeight() > 480) {
						BufferedImage scaled = ImageManipulator.getScaledImage(image, 640, 480);
						image.flush();
						image = null;

						getNormal().setResource(createImage(scaled));
						scaled.flush();
						scaled = null;
					} else {
						getNormal().setResource(createImage(image), RSRC_IMAGE_BESTFIT);
						image.flush();
						image = null;
					}
				}
			} finally {
				setPainting(true);
			}
			getNormal().flush();
		}

		public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
			if (mDesktopThread != null && mDesktopThread.isAlive())
				mDesktopThread.interrupt();

			mDesktopThread = new Thread() {
				public void run() {
					try {
						while (true) {
							synchronized (this) {
								update();
							}
							sleep(1000);
						}
					} catch (Exception ex) {
						Tools.logException(Desktop.class, ex, "Could not retrieve desktop");
					}
				}

				public void interrupt() {
					synchronized (this) {
						super.interrupt();
					}
				}
			};
			mDesktopThread.start();
			return super.handleEnter(arg, isReturn);
		}

		public boolean handleExit() {
			if (mDesktopThread != null && mDesktopThread.isAlive())
				mDesktopThread.interrupt();
			mDesktopThread = null;
			return super.handleExit();
		}

		public boolean handleKeyPress(int code, long rawcode) {
			return super.handleKeyPress(KEY_LEFT, rawcode);
		}

		private Thread mDesktopThread;
	}

	public static class DesktopFactory extends AppFactory {

		public void initialize() {

		}
	}
}