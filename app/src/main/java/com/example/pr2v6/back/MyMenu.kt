package com.example

import addNewDoctorFromMenu
import cancelAllConsultations
import cancelConsultation
import changeDoctorFromFile
import clearDoctorList
import deleteCertainDoctor
import deleteDoctorList
import findDoctorWithMessage
import payForConsultations
import printConsultations
import signUpForConsultation

class MyVariant(
    private val variantName: String,
    internal val variantAction: (input: Unit) -> Boolean,
)
{
    override fun toString(): String {
        return this.variantName
    }
}

class MyMenuVariants(
    private var variants: List<MyVariant>,
)
{
    fun getSize(): Int {
        return variants.size
    }

    fun printAllVariants() {
        for( (i, variant) in this.variants.withIndex()) {
            println("${i + 1}. $variant")
        }
    }

    fun getMaxVariantStringLength(): Int {
        return variants.size.toString().length
    }

    fun performAction(variantNumber: Int): Boolean {
        if( !CheckIfIndexInBounds(variantNumber) )
            return false

        return variants[variantNumber].variantAction.invoke( Unit )
    }


    fun parseVariantInput(givenString: String?): Boolean {
        if ( givenString.isNullOrBlank() )
            return false

        if (givenString.length > this.getMaxVariantStringLength() )
            return false

        val number: Int = givenString.toIntOrNull() ?: return false

        if( !CheckIfIndexInBounds(number - 1) ) {
            println("Такого пункта не существует")
            return false
        }
        return performAction(number - 1)
    }

    private fun CheckIfIndexInBounds(variantNumber: Int): Boolean {
        if (variantNumber < 0 || variantNumber >= this.getSize())
            return false
        return true
    }
}

class MyMenu() {
    private lateinit var mainMenu: MyMenuVariants
    private lateinit var subMenu: MyMenuVariants

    fun printAllVariants() : Unit {
        this.mainMenu.printAllVariants()
    }

    fun parseMenuVariantSelectionInput(givenString: String? ) : Boolean {
        return this.mainMenu.parseVariantInput(givenString)
    }

    fun subMenuSelected(): Unit {
        var numberString: String?

        println("Для выхода из подменю введите \"end\"")

        while( true ) {
            subMenu.printAllVariants()
            print("Введите номер пункта меню: ")
            val numberString: String? = readlnOrNull()
            if (numberString != null && numberString == "end") {
                return
            }

            var result: Boolean = subMenu.parseVariantInput( numberString )
            if( result == false )
                break
        }
    }


    init {
        val payElement: MyVariant = MyVariant(
            variantName = "Оплатить предварительные записи на приём",
            variantAction = { payForConsultations() }
        )

        val signElement: MyVariant = MyVariant(
            variantName = "Предварительно записаться на приём",
            variantAction = {
                signUpForConsultation()
                true
            }
        )

        val viewElement: MyVariant = MyVariant(
            variantName = "Посмотреть предварительные записи",
            variantAction = {
                printConsultations()
                true
            }
        )

        val cancelElement: MyVariant = MyVariant(
            variantName = "Отменить предварительную запись",
            variantAction = {
                cancelConsultation()
                true
            }
        )

        val cancelAllElements: MyVariant = MyVariant(
            variantName = "Отменить все предварительные записи",
            variantAction = {
                cancelAllConsultations()
                true
            }
        )

        val returnElement: MyVariant = MyVariant(
            variantName = "Вернуться",
            variantAction = {
                println("Возврат")
                false
            }
        )

        val subVariantList: List<MyVariant> = listOf(payElement, signElement, viewElement,
                                                     cancelElement, cancelAllElements, returnElement)
        this.subMenu = MyMenuVariants(subVariantList)

        val addElement: MyVariant = MyVariant(variantName = "Добавить доктора в файл",
                                              variantAction = {
                                                  var result = addNewDoctorFromMenu()
                                                  when(result) {
                                                      true -> println("Успешно добавлен")
                                                      false -> println("Добавление не удалось")
                                                  }
                                                  true
                                              } )
        val changeElement: MyVariant = MyVariant(variantName = "Изменить доктора из файла",
                                                 variantAction = {
                                                    changeDoctorFromFile()
                                                    true
                                                 } )
        val findElement: MyVariant = MyVariant(variantName = "Найти доктора по специальности и стоимости консультации",
                                            variantAction = {
                                                findDoctorWithMessage()
                                                true
                                            } )
        val deleteElement: MyVariant = MyVariant(variantName = "Удалить доктора из файла",
                                                 variantAction = {
                                                    deleteCertainDoctor()
                                                    true
                                                 } )
        val clearFile: MyVariant = MyVariant(variantName = "Очистить всё из файла",
                                             variantAction = {
                                                clearDoctorList()
                                                true
                                             })
        val deleteFile: MyVariant = MyVariant(variantName = "Удалить файл",
                                              variantAction = {
                                                deleteDoctorList()
                                                true
                                              })
        val registation: MyVariant = MyVariant(variantName = "Оформление записей на приём",
                                               variantAction = {
                                                subMenuSelected()
                                                true
                                               })

        val variantList: List<MyVariant> = listOf(addElement, changeElement, findElement,
                                                  deleteElement, clearFile, deleteFile, registation)
        this.mainMenu = MyMenuVariants(variantList)
    }


}