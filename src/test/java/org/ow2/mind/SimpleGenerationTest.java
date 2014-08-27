/**
 * Copyright (C) 2012 Schneider-Electric
 *
 * This file is part of "Mind Compiler" is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact: mind@ow2.org, sseyvoz@assystem.com
 *
 * Authors: Stéphane Seyvoz, Assystem (for Schneider-Electric)
 * Contributors: 
 */

package org.ow2.mind;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.objectweb.fractal.adl.Definition;
import org.objectweb.fractal.adl.util.FractalADLLogManager;
import org.ow2.mind.AbstractSimpleGenerationTest;
import org.ow2.mind.adl.annotation.predefined.Run;
import org.ow2.mind.annotation.AnnotationHelper;
import org.ow2.mind.io.OutputFileLocator;
import org.testng.annotations.Test;

public class SimpleGenerationTest extends AbstractSimpleGenerationTest {

	private static final String ADL_EXT = ".adl.h";
	private static final String ITF_EXT = ".itf.h";
	private static final String MAKE_EXT = ".make";
	protected static Logger logger = FractalADLLogManager.getLogger("simple-generation-test");

	@Override
	protected void initPath() {
		initSourcePath(getDepsDir("fractal/api/Component.itf").getAbsolutePath(),
				"src");
	}



	protected class TestCase {

		public String rootDir;
		public String adlName;
		public String optimCombo;
		public List<String> flags ;

		public TestCase(String rootDir,
				String adlName,
				String optimCombo,
				List<String> flags) {
			this.rootDir 		= rootDir;
			this.adlName 		= adlName;
			this.optimCombo 	= optimCombo ;
			this.flags 			= flags ;
		}

		public String toString(){
			String flagsStr;
			String optimStr;

			if ((flags != null) && (!flags.isEmpty()))
				flagsStr = ", flags=" + flags;
			else flagsStr = ", no flag";

			if ((optimCombo != null) && (!optimCombo.equals("")))
				optimStr = ", optimCombo=" + optimCombo;
			else optimStr = ", no optim";

			return "[TestCase] " + rootDir + "/" + adlName + optimStr + flagsStr;
		}

	}

	/**
	 * The most basic test.
	 */
	@Test(groups = {"simpleGeneration"})
	public void basicHelloworldTest()
			throws Exception {
		
		initPath();
		
		initContext(true);
		String adlName = "common.BasicTypeServer";

		List<String> flags = new ArrayList<String>();
		
		final Definition d = runner.load(adlName);
		final Run runAnno = AnnotationHelper.getAnnotation(d, Run.class);
		if (runAnno != null) {
			runner.addCFlags(flags);

			runner.compile(adlName, runAnno.executableName);

			// Testing .adl.h file existence.
			File adlOutputFile = runner.outputFileLocator.getCSourceOutputFile(PathHelper.fullyQualifiedNameToPath(adlName, ADL_EXT), runner.context);
			assertTrue(adlOutputFile.exists());

			// Testing .itf.h existence.
			File itfOutputFile = runner.outputFileLocator.getCSourceOutputFile(PathHelper.fullyQualifiedNameToPath("common.BasicTypes",ITF_EXT), runner.context);
			assertTrue(itfOutputFile.exists());
			
			// Testing .make existence.
			File makeOutputFile = runner.outputFileLocator.getCSourceOutputFile(PathHelper.fullyQualifiedNameToPath(adlName, MAKE_EXT), runner.context);
			assertTrue(makeOutputFile.exists());
			
			final int r = Runtime.getRuntime().exec("make -f" + makeOutputFile.getAbsolutePath()).waitFor();
			assertEquals(r, 0, "Unexpected return value");

		} else {
			if (logger.isLoggable(Level.FINE))
				logger.log(Level.FINE, "Skipped test on ADL " + adlName + " : no @Run annotation was found.");
		}

	}
}

