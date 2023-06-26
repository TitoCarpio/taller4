package com.erickcg.Parcial2.models.entities;


import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "songxplaylist")
public class SongXPlaylist {
	@Id
	@Column(name = "code")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private UUID code;
	
	@Column(name = "date_add")
	private Date date_added;
	
	//declarando las fk
	//cuando es ManytoMany el atributo debe de ser una lista
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "song_code", nullable = false)
//	@JsonIgnore
	private Song song;
	
	//cuando es ManytoMany el atributo debe de ser una lista
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "playlist_code", nullable = false)
	@JsonIgnore
	private Playlist playlist;

	public SongXPlaylist(Date date_added, Song song, Playlist playlist) {
		super();
		this.date_added = date_added;
		this.song = song;
		this.playlist = playlist;
	}

	


}
