package com.tekerasoft.tekeramarketplace.model.esdocument

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.tekerasoft.tekeramarketplace.config.NoArg
import jakarta.persistence.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.Setting

@Document(indexName = "search_index")
@Setting(settingPath = "static/es-settings.json")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArg
open class SearchItem(
    @Id
    open var id: String,

    @Field("name", type = FieldType.Text, analyzer = "custom_index", searchAnalyzer = "custom_search")
    open var name: String,

    @Field("slug", type = FieldType.Text, analyzer = "custom_index", searchAnalyzer = "custom_search")
    open var slug: String,

    @Field("companyId", type = FieldType.Text, analyzer = "custom_index", searchAnalyzer = "custom_search")
    open var companyId: String? = null,

    @Field("image_url", type = FieldType.Text)
    open var imageUrl: String? = null,

    @Field("item_type", type = FieldType.Text)
    open var itemType: SearchItemType,

    @Field("rate", type = FieldType.Double)
    open var rate: Double? = 0.0,

    )
