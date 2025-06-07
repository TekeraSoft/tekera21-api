package com.tekerasoft.tekeramarketplace.model.esdocument

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.Setting

@Document(indexName = "items_index")
@Setting(settingPath = "static/es-settings.json")
@JsonIgnoreProperties(ignoreUnknown = true)
open class Item(

    @Id
    open var id: String = "",

    @Field("name", type = FieldType.Text)
    open var name: String = "",

    @Field("price", type = FieldType.Double)
    open var price: Double = 0.0,

    @Field("brand", type = FieldType.Text)
    open var brand: String = "",

    @Field("category", type = FieldType.Keyword)
    open var category: String = "",
)
