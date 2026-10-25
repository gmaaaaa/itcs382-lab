package bikeshare;

public record Ride(String memberId, MemberType memberType, String bikeId, int minutes, int fee) {
}
