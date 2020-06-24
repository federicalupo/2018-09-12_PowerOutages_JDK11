package it.polito.tdp.poweroutages.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

import it.polito.tdp.poweroutages.model.Arco;
import it.polito.tdp.poweroutages.model.Evento;
import it.polito.tdp.poweroutages.model.Evento.TipoEvento;
import it.polito.tdp.poweroutages.model.Nerc;

public class PowerOutagesDAO {
	
	public List<Nerc> loadAllNercs(Map<Integer, Nerc> idMap) {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(n);
				idMap.put(n.getId(),n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}
	
	public List<Arco> getArchi(Map<Integer, Nerc> idMap){ //peso 0
		String sql = "select nerc_one, nerc_two " + 
				"from NercRelations";
		
		List<Arco> archi = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n1 = idMap.get(res.getInt("nerc_one"));
				Nerc n2 = idMap.get(res.getInt("nerc_two"));
				
				if(n1!=null && n2!=null) {
					archi.add(new Arco(n1, n2, 0));
				}
			}

			conn.close();
			return archi;

		} catch (SQLException e) {
			throw new RuntimeException(e);
			 
		}

	}
	
	public List<Arco> getArchiPeso(Map<Integer, Nerc> idMap){
		String sql = "select nerc_one, nerc_two, count(distinct year(p1.`date_event_began`), month(p1.`date_event_began`)) as conta " + 
				"from NercRelations as n, PowerOutages as p1, PowerOutages as p2 " + 
				"where n.`nerc_one` = p1.`nerc_id`  " + 
				"and n.`nerc_two` = p2.`nerc_id` " + 
				"and year(p1.`date_event_began`) = year(p2.`date_event_began`) " + 
				"and month(p1.`date_event_began`) = month(p2.`date_event_began`) " + 
				"group by nerc_one, nerc_two";
		List<Arco> archi = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n1 = idMap.get(res.getInt("nerc_one"));
				Nerc n2 = idMap.get(res.getInt("nerc_two"));
				Integer peso = res.getInt("conta");
				
				if(n1!=null && n2!=null) {
					archi.add(new Arco(n1, n2, peso));
				}
			}

			conn.close();
			return archi;

		} catch (SQLException e) {
			throw new RuntimeException(e);
			 
		}
	}
	
	public PriorityQueue<Evento> listaEventi(Map<Integer, Nerc> idMap){
		String sql = "select nerc_id, date_event_began, date_event_finished " + 
				"from powerOutages";
		
		
		PriorityQueue<Evento> eventi = new PriorityQueue<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = idMap.get(res.getInt("nerc_id"));
				LocalDateTime inizio= res.getTimestamp("date_event_began").toLocalDateTime();
				LocalDateTime fine= res.getTimestamp("date_event_finished").toLocalDateTime();
				Long giorni = Duration.between(inizio, fine).toDays(); 
				
				if(n!=null) {
					Evento e = new Evento(TipoEvento.INIZIO_INTERRUZIONE, n, inizio, fine, inizio, giorni);
					
					eventi.add(e);
					
				}
			}

			conn.close();
			
			return eventi;

		} catch (SQLException e) {
			throw new RuntimeException(e);
			 
		}
	}
}
