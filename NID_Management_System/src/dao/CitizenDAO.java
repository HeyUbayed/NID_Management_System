package dao;
import db.DBConnection;
import model.Citizen;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitizenDAO {

    public boolean registerCitizen(Citizen citizen) {
        String sql = "INSERT INTO citizen (full_name, father_name, mother_name, date_of_birth, " +
                "gender, blood_group, religion, phone_number, email, division, district, " +
                "upazila, postal_code, password, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return false;

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, citizen.getFullName());
            pstmt.setString(2, citizen.getFatherName());
            pstmt.setString(3, citizen.getMotherName());
            pstmt.setDate(4, Date.valueOf(citizen.getDateOfBirth()));
            pstmt.setString(5, citizen.getGender());
            pstmt.setString(6, citizen.getBloodGroup());
            pstmt.setString(7, citizen.getReligion());
            pstmt.setString(8, citizen.getPhoneNumber());
            pstmt.setString(9, citizen.getEmail());
            pstmt.setString(10, citizen.getDivision());
            pstmt.setString(11, citizen.getDistrict());
            pstmt.setString(12, citizen.getUpazila());
            pstmt.setString(13, citizen.getPostalCode());
            pstmt.setString(14, citizen.getPassword());
            pstmt.setString(15, citizen.getStatus());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error registering citizen: " + e.getMessage());
            return false;
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    public Citizen loginCitizen(String email, String password) {
        String sql = "SELECT * FROM citizen WHERE email = ? AND password = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return null;

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractCitizenFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error during citizen login: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return null;
    }

    public List<Citizen> getAllCitizens() {
        List<Citizen> citizens = new ArrayList<>();
        String sql = "SELECT * FROM citizen ORDER BY citizen_id DESC";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return citizens;

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                citizens.add(extractCitizenFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching all citizens: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return citizens;
    }

    public boolean updateCitizen(Citizen citizen) {
        String sql = "UPDATE citizen SET full_name = ?, father_name = ?, mother_name = ?, " +
                "date_of_birth = ?, gender = ?, blood_group = ?, religion = ?, phone_number = ?, " +
                "email = ?, division = ?, district = ?, upazila = ?, postal_code = ?, status = ? WHERE citizen_id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return false;

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, citizen.getFullName());
            pstmt.setString(2, citizen.getFatherName());
            pstmt.setString(3, citizen.getMotherName());
            pstmt.setDate(4, Date.valueOf(citizen.getDateOfBirth()));
            pstmt.setString(5, citizen.getGender());
            pstmt.setString(6, citizen.getBloodGroup());
            pstmt.setString(7, citizen.getReligion());
            pstmt.setString(8, citizen.getPhoneNumber());
            pstmt.setString(9, citizen.getEmail());
            pstmt.setString(10, citizen.getDivision());
            pstmt.setString(11, citizen.getDistrict());
            pstmt.setString(12, citizen.getUpazila());
            pstmt.setString(13, citizen.getPostalCode());
            pstmt.setString(14, citizen.getStatus());
            pstmt.setInt(15, citizen.getCitizenId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating citizen: " + e.getMessage());
            return false;
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    public boolean deleteCitizen(int citizenId) {
        String sql = "DELETE FROM citizen WHERE citizen_id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return false;

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, citizenId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting citizen: " + e.getMessage());
            return false;
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    public boolean approveCitizen(int citizenId, String nidNumber) {
        String sql = "UPDATE citizen SET status = 'Approved', nid_number = ? WHERE citizen_id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return false;

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nidNumber);
            pstmt.setInt(2, citizenId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error approving citizen: " + e.getMessage());
            return false;
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    public Citizen getCitizenById(int citizenId) {
        String sql = "SELECT * FROM citizen WHERE citizen_id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return null;

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, citizenId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractCitizenFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching citizen by ID: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return null;
    }

    public List<Citizen> searchCitizens(String searchTerm) {
        List<Citizen> citizens = new ArrayList<>();
        String sql = "SELECT * FROM citizen WHERE full_name LIKE ? OR email LIKE ? OR nid_number LIKE ? OR phone_number LIKE ? ORDER BY citizen_id DESC";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return citizens;

            PreparedStatement pstmt = conn.prepareStatement(sql);
            String likeTerm = "%" + searchTerm + "%";
            pstmt.setString(1, likeTerm);
            pstmt.setString(2, likeTerm);
            pstmt.setString(3, likeTerm);
            pstmt.setString(4, likeTerm);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                citizens.add(extractCitizenFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error searching citizens: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return citizens;
    }

    public boolean isEmailExists(String email) {
        String sql = "SELECT citizen_id FROM citizen WHERE email = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return false;

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
            return false;
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    public boolean isPhoneNumberExists(String phoneNumber) {
        String sql = "SELECT citizen_id FROM citizen WHERE phone_number = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return false;

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phoneNumber);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Error checking phone number existence: " + e.getMessage());
            return false;
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    public boolean rejectCitizen(int citizenId) {
        String sql = "UPDATE citizen SET status = 'Rejected', nid_number = NULL WHERE citizen_id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return false;

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, citizenId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error rejecting citizen: " + e.getMessage());
            return false;
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    public Citizen getCitizenByNid(String nidNumber) {
        String sql = "SELECT * FROM citizen WHERE nid_number = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return null;

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nidNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractCitizenFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching citizen by NID: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return null;
    }

    private Citizen extractCitizenFromResultSet(ResultSet rs) throws SQLException {
        Citizen citizen = new Citizen();
        citizen.setCitizenId(rs.getInt("citizen_id"));
        citizen.setFullName(rs.getString("full_name"));
        citizen.setFatherName(rs.getString("father_name"));
        citizen.setMotherName(rs.getString("mother_name"));

        Date dob = rs.getDate("date_of_birth");
        citizen.setDateOfBirth(dob != null ? dob.toLocalDate() : null);

        citizen.setGender(rs.getString("gender"));
        citizen.setBloodGroup(rs.getString("blood_group"));
        citizen.setReligion(rs.getString("religion"));
        citizen.setPhoneNumber(rs.getString("phone_number"));
        citizen.setEmail(rs.getString("email"));
        citizen.setDivision(rs.getString("division"));
        citizen.setDistrict(rs.getString("district"));
        citizen.setUpazila(rs.getString("upazila"));
        citizen.setPostalCode(rs.getString("postal_code"));
        citizen.setNidNumber(rs.getString("nid_number"));
        citizen.setPassword(rs.getString("password"));
        citizen.setStatus(rs.getString("status"));

        Date regDate = rs.getDate("registration_date");
        citizen.setRegistrationDate(regDate != null ? regDate.toLocalDate() : null);

        return citizen;
    }
}