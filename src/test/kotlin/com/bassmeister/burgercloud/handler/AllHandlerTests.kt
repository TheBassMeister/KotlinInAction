package com.bassmeister.burgercloud.handler

import org.junit.platform.runner.JUnitPlatform
import org.junit.platform.suite.api.SelectPackages
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.springframework.test.context.junit4.SpringRunner


@RunWith(JUnitPlatform::class)
@SelectPackages("com.bassmeister.burgercloud.handler")
class AllHandlerTests