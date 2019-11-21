package com.boiqin.pdfscript

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.bundling.Zip
import java.io.File

/**
 * plugin entry
 */
class FdfLibraryPlugin : Plugin<Project> {


    private lateinit var project: Project
    private lateinit var rootPath: String


    override fun apply(project: Project) {
        println("this is pdfscript ,dealing with " + project.name)
        val taskList = project.gradle.startParameter.taskNames
        for (task in taskList) {
            println("execute gradle task: $task")
        }
        this.project = project
        rootPath = "${project.rootDir.absolutePath}/${project.name}"



        project.afterEvaluate {
            project.tasks.getByName("assembleDebug") {
                val aarType = "debug"
                val zipTask = zipTask(aarType)
                val deleteTask = deleteTask(aarType)
                val unzipTask = unzipTask(aarType)
                zipTask.dependsOn(deleteTask)
                deleteTask.dependsOn(unzipTask)
                it.finalizedBy(zipTask)
            }
            project.tasks.getByName("assembleRelease") {
                val aarType = "release"
                val zipTask = zipTask(aarType)
                val deleteTask = deleteTask(aarType)
                val unzipTask = unzipTask(aarType)
                zipTask.dependsOn(deleteTask)
                deleteTask.dependsOn(unzipTask)
                it.finalizedBy(zipTask)
            }
//            val android = project.extensions.getByName("android") as LibraryExtension
//            android.libraryVariants.all {
//                Utils.logInfo("start process: ${it.flavorName}${it.buildType.name.capitalize()}")
//                processVariant(it)
//            }
        }
    }

    private fun unzipTask(aarType:String): Task {
        val taskName = "unzip${aarType.capitalize()}Task"
        return project.tasks.create<Copy>(taskName, Copy::class.java).run {
            println("the task is: $taskName")
            // 原始aar包文件
            val zipFile = File("${rootPath}/build/outputs/aar/pdf-${aarType}.aar")
            // 解压缩目标目录
            val outputDir = File("${rootPath}/build/outputs/aar/pdf")
            val jarTree = project.zipTree(zipFile)
            // 从jar的目录树中输出到目标目录
            from(jarTree)
            into(outputDir)
        }
    }

    private fun deleteTask(aarType:String): Task {
        val taskName = "delete${aarType.capitalize()}Task"
        return project.tasks.create<Delete>(taskName, Delete::class.java).run {
            println("the task is: $taskName")
            val zipFile = File("${rootPath}/build/outputs/aar/pdf-${aarType}.aar")
            delete(zipFile)
            val jniDir = File("${rootPath}/build/outputs/aar/pdf/jni")
            delete(jniDir)
        }
    }

    private fun zipTask(aarType:String): Task {
        val taskName = "zip${aarType.capitalize()}Task"
        return project.tasks.create<Zip>(taskName, Zip::class.java).run {
            println("the task is: $taskName")
            from("${rootPath}/build/outputs/aar/pdf")
            archiveBaseName.set("pdf-${aarType}")
            archiveExtension.set("aar")
            destinationDirectory.set(File("${rootPath}/build/outputs/aar"))
            doLast {
                project.delete("${rootPath}/build/outputs/aar/pdf")
            }
        }
    }
}
