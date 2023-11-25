package ui.elements

import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import misc.SKIN
import utils.Transform4

class Transform4Field(private val value: Transform4) : Dropdown("Transform 4") {

    init {
        // Titre avec une petite flèche pour replier/déplier la catégorie
        val titleButton = TextButton("Transform 4", SKIN) // Remplacez "yourSkin" par votre skin

        // Champ Location
        val locationField = Vector4Field("Location", value.location) // Remplacez "yourLocationValueRef" par votre référence de valeur

        // Champ Rotation
        val rotationField = Rotator4Field("Rotation", value.rotation) // Remplacez "yourRotationValueRef" par votre référence de valeur

        // Champ Scale
        val scaleField = Vector4Field("Scale", value.scale) // Remplacez "yourScaleValueRef" par votre référence de valeur

        // Ajoutez les éléments à la table
        /*add(titleButton).colspan(2).expandX().fillX().padBottom(0f)
        row().pad(2f)
        add(locationField).colspan(2).expandX().fillX().padBottom(0f)
        row().pad(2f)
        add(rotationField).colspan(2).expandX().fillX().padBottom(0f)
        row().pad(2f)
        add(scaleField).colspan(2).expandX().fillX().padBottom(0f)*/
        addContent(locationField)
        addContent(rotationField)
        addContent(scaleField)
    }
}