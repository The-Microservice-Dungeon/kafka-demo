package de.pschm.kafka.demo.core.util

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class BeanUtil : ApplicationContextAware {
    companion object {
        var context: ApplicationContext? = null
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

    /**
     * Returns the Spring managed bean instance of the given class type if it exists.
     * Returns null otherwise.
     */
    fun <T> getBean(beanClass: Class<T>): T? {
        return context?.getBean(beanClass)
    }
}