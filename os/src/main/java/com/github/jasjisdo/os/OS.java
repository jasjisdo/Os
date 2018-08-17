package com.github.jasjisdo.os;

import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;

/**
 * This exports:
 * - all functions from posix, nt, os2, or ce, e.g. unlink, stat, etc.
 * - os.path is one of the modules posixpath, or ntpath
 * - os.name is 'posix', 'nt', 'os2', 'ce' or 'riscos'
 * - os.curdir is a string representing the current directory ('.' or ':')
 * - os.pardir is a string representing the parent directory ('..' or '::')
 * - os.sep is the (or a most common) pathname separator ('/' or ':' or '\\')
 * - os.extsep is the extension separator ('.' or '/')
 * - os.altsep is the alternate pathname separator (None or '/')
 * - os.pathsep is the component separator used in $PATH etc
 * - os.linesep is the line separator in text files ('\r' or '\n' or '\r\n')
 * - os.defpath is the default search path for executables
 * - os.devnull is the file path of the null device ('/dev/null', etc.)
 */
public class OS {

    public enum NAME {

        WINDOWS("win"),
        MACOS("mac"),
        SOLARIS("sunos"),
        LINUX("linux"),
        UNIX("unix"),
        UNKNOWN("");

        private final String indicator;

        NAME(String indicator) {
            this.indicator = indicator;
        }

    }

    /**
     * @return
     */
    public static NAME name() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains(NAME.WINDOWS.indicator)) return NAME.WINDOWS;
        if (osName.contains(NAME.MACOS.indicator)) return NAME.MACOS;
        if (osName.contains(NAME.LINUX.indicator)) return NAME.LINUX;
        if (osName.contains(NAME.UNIX.indicator)) return NAME.UNIX;
        if (osName.contains(NAME.SOLARIS.indicator)) return NAME.SOLARIS;
        return NAME.UNKNOWN;
    }

    public static String root() {
        if (name().equals(NAME.WINDOWS)) return System.getenv("SYSTEMDRIVE");
        else return separator();
    }

    /**
     * A string representing the current directory.
     *
     * @return "." or ":"
     */
    public static String currentDirectory() {
        if (name().equals(NAME.WINDOWS)
                || name().equals(NAME.MACOS)
                || name().equals(NAME.LINUX)
                || name().equals(NAME.UNIX)
                || name().equals(NAME.SOLARIS)) {
            return ".";
        } else {
            return ":";
        }
    }

    /**
     * A string representing the parent directory.
     *
     * @return ".." or "::"
     */
    public static String parentDirectory() {
        if (name().equals(NAME.WINDOWS)
                || name().equals(NAME.MACOS)
                || name().equals(NAME.LINUX)
                || name().equals(NAME.UNIX)
                || name().equals(NAME.SOLARIS)) {
            return "..";
        } else {
            return "::";
        }
    }

    public static String separator() {
        return File.separator;
    }

    public static String extensionSeparator() {
        if (name().equals(NAME.WINDOWS)
                || name().equals(NAME.MACOS)
                || name().equals(NAME.LINUX)
                || name().equals(NAME.UNIX)
                || name().equals(NAME.SOLARIS)) {
            return ".";
        } else {
            return "/";
        }
    }

    public static String pathSeparator() {
        return File.pathSeparator;
    }

    public static String lineSeparator() {
        return System.lineSeparator();
    }

    public static String getDefaultSearchPath() {
        if (name().equals(NAME.WINDOWS)) {
            return currentDirectory() + pathSeparator() + root() + separator() + "bin";
        } else
            return separator() + "bin" + pathSeparator() + separator() + "usr" + separator() + "bin";
    }

    public static int system(String exec, String... args) {
        final Commandline cmd = new Commandline();
        cmd.setExecutable(exec);
        cmd.addArguments(args);
        try {
            return CommandLineUtils.executeCommandLine(cmd, null, null);
        } catch (CommandLineException e) {
            e.printStackTrace();
            return Integer.MIN_VALUE;
        }
    }

    public static void main(String[] args) {
        System.out.println(OS.name());
        System.out.println(OS.currentDirectory());
        System.out.println(OS.parentDirectory());
        System.out.println(OS.separator());
        System.out.println(OS.extensionSeparator());
        System.out.println(OS.pathSeparator());
        System.out.println(OS.getDefaultSearchPath());
        if (OS.name().equals(NAME.WINDOWS)) System.out.println(OS.system("sc", "query"));
    }

}

