package com.javalec.ex.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.javalec.ex.dto.BDto;

public class BDao {
	DataSource dataSource;
	
	public BDao() {
		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/Oracle11g");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void write(String bName,String bTitle,String bContent) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String query = "insert into mvc_board (bId, bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) values (mvc_board_seq.nextval, ?, ?, ?, 0, mvc_board_seq.currval, 0, 0 )";
		
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, bName);
			preparedStatement.setString(2, bTitle);
			preparedStatement.setString(3, bContent);
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				preparedStatement.close();
				connection.close();
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public ArrayList<BDto> list(){
		ArrayList<BDto> dtos = new ArrayList<BDto>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "select bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent from mvc_board order by bGroup desc, bStep asc";
		
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
	
				int bId = resultSet.getInt("bId");
				String bName = resultSet.getString("bName");
				String bTitle = resultSet.getString("bTitle");
				String bContent = resultSet.getString("bContent");
				Timestamp bDate = resultSet.getTimestamp("bDate");
				int bHit = resultSet.getInt("bHit");
				int bGroup = resultSet.getInt("bGroup");
				int bStep = resultSet.getInt("bStep");
				int bIndent = resultSet.getInt("bIndent");
				
				BDto dto = new BDto(bId,bName,bTitle,bContent,bDate,bHit,bGroup,bStep,bIndent);
				dtos.add(dto);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();
				}catch (Exception e2) {
					e2.printStackTrace();
				}
		}
		return dtos;
	}
	public BDto contentView(String strID) {
		upHit(strID);
		
		BDto dto = null;
		Connection cnt = null;
		PreparedStatement psm=null;
		ResultSet rs = null;
		String query="select * from mvc_board where bId =?";
		
		try {
			cnt = dataSource.getConnection();
			psm = cnt.prepareStatement(query);
			psm.setInt(1, Integer.parseInt(strID));
			rs = psm.executeQuery();
			
			while(rs.next()) {
				int bId = rs.getInt("bId");
				String bName = rs.getString("bName");
				String bTitle = rs.getString("bTitle");
				String bContent = rs.getString("bContent");
				Timestamp bDate = rs.getTimestamp("bDate");
				int bHit = rs.getInt("bHit");
				int bGroup = rs.getInt("bGroup");
				int bStep = rs.getInt("bStep");
				int bIndent = rs.getInt("bIndent");
				
				dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);	
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				psm.close();
				cnt.close();
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return dto;
	}
	public void modify(String bId, String bName, String bTitle, String bContent) {
		Connection cnt =null;
		PreparedStatement psm = null;
		String query="update mvc_board set bName=?, bTitle = ?,bContent = ? where bId = ?";
		
		try {
			cnt=dataSource.getConnection();
			psm = cnt.prepareStatement(query);
			psm.setString(1, bName);
			psm.setString(2, bTitle);
			psm.setString(3, bContent);
			psm.setInt(4, Integer.parseInt(bId));
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				psm.close();
				cnt.close();
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public void delete(String bId,String bGroup) {
		Connection cnt = null;
		PreparedStatement psm = null;
		String query = "delete from mvc_board where bid = ? or bGroup = ?";
		
		try {
			cnt=dataSource.getConnection();
			psm = cnt.prepareStatement(query);
			psm.setInt(1,Integer.parseInt(bId));
			psm.setInt(2,Integer.parseInt(bGroup));
			
			
		}catch (Exception e) {
			e.printStackTrace();
			
		}finally {
			try {
				psm.close();
				cnt.close();
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public BDto reply_view(String str) {
		BDto dto = null;
		Connection cnt = null;
		PreparedStatement psm = null;
		ResultSet rs = null;
		String query="select * from mvc_board where bId = ?";
		try {
			cnt = dataSource.getConnection();
			psm = cnt.prepareStatement(query);
			psm.setInt(1, Integer.parseInt(str));
			rs = psm.executeQuery();
			
			while(rs.next()) {
				int bId = rs.getInt("bId");
				String bName = rs.getString("bName");
				String bTitle = rs.getString("bTitle");
				String bContent = rs.getString("bContent");
				Timestamp bDate = rs.getTimestamp("bDate");
				int bHit = rs.getInt("bHit");
				int bGroup = rs.getInt("bGroup");
				int bStep = rs.getInt("bStep");
				int bIndent = rs.getInt("bIndent");
				
				dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
				}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				psm.close();
				cnt.close();
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return dto;
	}
	
	public void reply(String bId, String bName, String bTitle, String bContent, String bGroup, String bStep, String bIndent) {
		replyShape(bGroup,bStep);
		Connection cnt = null;
		PreparedStatement psm = null;
		String query = "insert into mvc_board (bId, bName, bTitle, bContent, bGroup, bStep, bIndent) values (mvc_board_seq.nextval, ?, ?, ?, ?, ?, ?)";
		try {
			cnt = dataSource.getConnection();
			psm = cnt.prepareStatement(query);
			
			psm.setString(1, bName);
			psm.setString(2, bTitle);
			psm.setString(3, bContent);
			psm.setInt(4, Integer.parseInt(bGroup));
			psm.setInt(5, Integer.parseInt(bStep)+1);
			psm.setInt(6, Integer.parseInt(bIndent)+1);
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}finally {
			try {
				psm.close();
				cnt.close();
			}catch (Exception e2) {
				e2.printStackTrace();// TODO: handle exception
			}
		}
	}
	
	private void replyShape(String strGroup, String strStep) {
		Connection cnt = null;
		PreparedStatement psm = null;
		String query = "update mvc_board set bStep = bStep + 1 where bGroup = ? and bStep > ?";
		try {
			cnt = dataSource.getConnection();
			psm = cnt.prepareStatement(query);
			psm.setInt(1, Integer.parseInt(strGroup));
			psm.setInt(2, Integer.parseInt(strStep));
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}finally {
			try {
				psm.close();
				cnt.close();
			}catch (Exception e2) {
				e2.printStackTrace();// TODO: handle exception
			}
		}
	}
	
	private void upHit(String bId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String query="update mvc_board set bHit = bHit + 1 where bId = ?";
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, bId);
			
			int rn = preparedStatement.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				preparedStatement.close();
				connection.close();
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
}
