package com.weather.model;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;
import org.json.JSONObject;

import com.weather.utilities.IconManager;

@Entity
@Table(schema = "public", name = "weather_log")
public class WeatherLog {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weather_log_id_generator")
	@SequenceGenerator(name = "weather_log_id_generator", sequenceName = "weather_log_id", initialValue = 1, allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	@Column(name = "temperature", unique = false, length = 5, nullable = false)
	private double temperature;

	@Column(name = "wind_speed", unique = false, length = 5, nullable = false)
	private double windSpeed;

	@Column
	@UpdateTimestamp
	private LocalDateTime lastUpdated;

	@Column(name = "conditionType", unique = false, length = 50, nullable = false)
	private String conditionType;

	@Column(name = "source_id")
	private int sourceId;

	@ManyToOne
	@JoinColumn(name = "location_id")
	private Location location;

	private String dayOrNight;

	public WeatherLog() {

	}

	public WeatherLog(double temperature, double windSpeed, String conditionType, Location location, SourceType source) {
		this.temperature = temperature;
		this.windSpeed = windSpeed;
		this.conditionType = conditionType;
		this.location = location;
		this.sourceId = source.getValue();
	}

	public JSONObject toJson() {
		DecimalFormat df = new DecimalFormat("#.#");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		JSONObject weatherJson = new JSONObject();
		weatherJson.put("temperature", df.format(this.temperature));
		weatherJson.put("wind-speed", df.format(this.windSpeed));
		weatherJson.put("condition", this.conditionType);
		weatherJson.put("last-updated", this.lastUpdated.format(formatter));
		weatherJson.put("icon", IconManager.getIcon(this.conditionType));
		return weatherJson;
	}

	/**
	 * @return the dayOrNight
	 */
	public String getDayOrNight() {
		return dayOrNight;
	}

	/**
	 * @param dayOrNight
	 *            the dayOrNight to set
	 */
	public void setDayOrNight(String dayOrNight) {
		this.dayOrNight = dayOrNight;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the temperature
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * @param temperature
	 *            the temperature to set
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	/**
	 * @return the windSpeed
	 */
	public double getWindSpeed() {
		return windSpeed;
	}

	/**
	 * @param windSpeed
	 *            the windSpeed to set
	 */
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	/**
	 * @return the lastUpdated
	 */
	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * @param lastUpdated
	 *            the lastUpdated to set
	 */
	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	/**
	 * @return the conditionType
	 */
	public String getConditionType() {
		return conditionType;
	}

	/**
	 * @param conditionType
	 *            the conditionType to set
	 */
	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public SourceType getSource() {
		return SourceType.parse(this.sourceId);
	}

	public void setRight(SourceType source) {
		this.sourceId = source.getValue();
	}
	
	public enum SourceType {

		SOURCE1(1), SOURCE2(2), SOURCE3(3);

		private int value;

		SourceType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static SourceType parse(int id) {
			SourceType right = null; // Default
			for (SourceType item : SourceType.values()) {
				if (item.getValue() == id) {
					right = item;
					break;
				}
			}
			return right;
		}

	}


}
