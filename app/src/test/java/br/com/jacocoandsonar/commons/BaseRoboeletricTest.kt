package br.com.jacocoandsonar.commons

import org.robolectric.annotation.Config

@Config(sdk = [31], instrumentedPackages = ["androidx.loader.content"])
abstract class BaseRoboeletricTest {}