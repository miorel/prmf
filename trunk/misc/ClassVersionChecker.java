/*
 * Copyright (C) 2010 Miorel-Lucian Palii <mlpalii@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Miorel-Lucian Palii
 */
public class ClassVersionChecker {
	public static void main(String[] arg) throws IOException {
		for(String s: arg) {
			InputStream stream = new FileInputStream(s);
			System.out.print(s);
			if(readUnsignedShort(stream) == 0xcafe && readUnsignedShort(stream) == 0xbabe) {
				int minor = readUnsignedShort(stream);
				int major = readUnsignedShort(stream);
				System.out.print(" - format " + major + "." + minor);

				if(major == 45 && minor == 3)
					System.out.print(" (Java 1.0/1.1)");
				else if(major == 46 && minor == 0)
					System.out.print(" (Java 1.2)");
				else if(major == 47 && minor == 0)
					System.out.print(" (Java 1.3)");
				else if(major == 48 && minor == 0)
					System.out.print(" (Java 1.4)");
				else if(major == 49 && minor == 0)
					System.out.print(" (Java 1.5)");
				else if(major == 50 && minor == 0)
					System.out.print(" (Java 1.6)");

				System.out.println();
			}
			else
				System.out.println(" - not a class file!");
		}
	}

	private static int readUnsignedShort(InputStream stream) throws IOException {
		return (stream.read() << 8) + stream.read();
	}
}
