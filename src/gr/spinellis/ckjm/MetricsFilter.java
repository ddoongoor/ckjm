package gr.spinellis.jmetrics;

import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import org.apache.bcel.Repository;
import org.apache.bcel.Constants;
import org.apache.bcel.util.*;
import java.io.*;
import java.util.*;

/**
 * Convert a list of classes into their metrics.
 * Read from the standard input lines containing a class file
 * name or a jar file name, followed by a space and a class file name.
 * Display on the standard output the name of each class, followed by its
 * six Chidamber Kemerer metrics.
 *
 * @see ClassMetrics
 * @version $Id: \\dds\\src\\Research\\ckjm.RCS\\src\\gr\\spinellis\\ckjm\\MetricsFilter.java,v 1.2 2005/02/18 12:30:43 dds Exp $
 * @author <a href="http://www.spinellis.gr">Diomidis Spinellis</a>
 */
public class MetricsFilter {
	/**
	  * Load and parse the specified class.
	  * The class specification can be either a class file name, or
	  * a jarfile, followed by space, followed by a class file name.
	  */
	static JavaClass parseClass(String clspec) {
		int spc;
		JavaClass jc = null;

		if ((spc = clspec.indexOf(' ')) != -1) {
			String jar = clspec.substring(0, spc);
			clspec = clspec.substring(spc + 1);
			try {
				jc = new ClassParser(jar, clspec).parse();
			} catch (IOException e) {
				System.err.println("Error loading " + clspec + " from " + jar + ": " + e);
			}
		} else {
			try {
				jc = new ClassParser(clspec).parse();
			} catch (IOException e) {
				System.err.println("Error loading " + clspec + ": " + e);
			}
		}
		return jc;
	}

	public static void main(String[] argv) {
		ClassMap cm = new ClassMap();
		String    name = argv[0];

		for (int i = 0; i < argv.length; i++) {
			JavaClass jc = parseClass(argv[i]);
			if (jc != null) {
				ClassVisitor visitor = new ClassVisitor(jc, cm);
				visitor.start();
				visitor.end();
			}
		}
		cm.printMetrics(System.out);
	}
}
