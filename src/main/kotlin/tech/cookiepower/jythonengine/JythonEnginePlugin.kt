package tech.cookiepower.jythonengine

import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

@RuntimeDependency("org.python:jython-standalone:2.7.2")
object JythonEnginePlugin : Plugin(){
    @Config
    lateinit var config : Configuration
        private set
}