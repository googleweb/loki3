# loki3
this is a copy of loki render(https://sourceforge.net/projects/loki-render/).

there are two problems:

1.The output dir has nothing,bacause the rendered file path is around with single quotes (') in new blender,so loki obtains a wrong file name with single quotes(e.g. 001.png'),so loki
can not move tmp render file to output dir.below is my correct codes in /loki/src/net/whn/loki/CL/CLHelper.java#L85.

    public static String blender_getRenderedFileName(String stdout) {
        //old example "Saved: /home/daniel/.loki/tmp/0001.png Time: 00:00.31" 
        //new example "Saved: '/home/googlewell/.loki/tmp/0005.png' Time: 00:00.3"

        String tmp = null;

        Pattern pattern = Pattern.compile("Saved:\\s('?\\S*.png'?)");
        Matcher matcher = pattern.matcher(stdout);

        if (matcher.find()) {
            tmp = matcher.group(1);
            if (tmp.startsWith("'") && tmp.endsWith("'")) {
                tmp = tmp.replace("'", "");
            }
            return tmp;
        }
        return null;
}


2.There have no render time in the grunts list and job details list.The same reason,the bledner render console output letters have changed.
below is my correct codes in /loki/src/net/whn/loki/CL/CLHelper.java#L104.

    public static String extractBlenderRenderTime(String stdout) {
        Pattern pattern = Pattern.compile("\\sTime:\\s(\\d{2}:\\d{2}.\\d{2})\\s\\(Saving:\\s\\d{2}:\\d{2}.\\d{2}\\)");
        Matcher matcher = pattern.matcher(stdout);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
}
