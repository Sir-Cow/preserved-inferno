package sircow.preservedinferno.other;

public record SemVer(int major, int minor, int patch, String beta) {

    public static SemVer parse(String raw) {
        if (raw.startsWith("beta-")) {
            raw = raw.substring("beta-".length()) + "-beta";
        }

        String[] mainParts = raw.split("-", 2);
        String[] nums = mainParts[0].split("\\.");

        if (nums.length != 3) return null;

        try {
            int major = Integer.parseInt(nums[0]);
            int minor = Integer.parseInt(nums[1]);
            int patch = Integer.parseInt(nums[2]);

            String prerelease = mainParts.length == 2 ? mainParts[1] : null;

            return new SemVer(major, minor, patch, prerelease);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public int compareTo(SemVer other) {
        if (this.major != other.major) return Integer.compare(this.major, other.major);
        if (this.minor != other.minor) return Integer.compare(this.minor, other.minor);
        if (this.patch != other.patch) return Integer.compare(this.patch, other.patch);
        if (this.beta == null && other.beta != null) return 1;
        if (this.beta != null && other.beta == null) return -1;
        if (this.beta == null) return 0;

        return this.beta.compareTo(other.beta);
    }
}
