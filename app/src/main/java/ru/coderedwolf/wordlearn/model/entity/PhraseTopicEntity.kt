package ru.coderedwolf.wordlearn.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author CodeRedWolf. Date 14.06.2019.
 */
@Entity
data class PhraseTopicEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val title: String,
    val isStudy: Boolean
)