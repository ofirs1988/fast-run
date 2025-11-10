package com.example.terminalicon

object CommandTemplates {
    data class Template(
        val name: String,
        val description: String,
        val commands: List<String>,
        val icon: String = "default",
        val envVars: Map<String, String> = emptyMap()
    )

    val templates = listOf(
        // NPM Templates
        Template(
            name = "NPM Install",
            description = "Install project dependencies",
            commands = listOf("npm install"),
            icon = "green"
        ),
        Template(
            name = "NPM Dev Server",
            description = "Start development server",
            commands = listOf("npm run dev"),
            icon = "blue"
        ),
        Template(
            name = "NPM Build",
            description = "Build for production",
            commands = listOf("npm run build"),
            icon = "yellow"
        ),
        Template(
            name = "NPM Test",
            description = "Run tests",
            commands = listOf("npm test"),
            icon = "purple"
        ),
        Template(
            name = "NPM Clean Install",
            description = "Clean install dependencies",
            commands = listOf(
                "rm -rf node_modules package-lock.json",
                "npm install"
            ),
            icon = "red"
        ),

        // Docker Templates
        Template(
            name = "Docker Build",
            description = "Build Docker image",
            commands = listOf("docker build -t {project_name} ."),
            icon = "blue"
        ),
        Template(
            name = "Docker Run",
            description = "Run Docker container",
            commands = listOf("docker run -p 3000:3000 {project_name}"),
            icon = "green"
        ),
        Template(
            name = "Docker Compose Up",
            description = "Start services with docker-compose",
            commands = listOf("docker-compose up -d"),
            icon = "blue"
        ),
        Template(
            name = "Docker Compose Down",
            description = "Stop services",
            commands = listOf("docker-compose down"),
            icon = "red"
        ),
        Template(
            name = "Docker Clean",
            description = "Remove all stopped containers",
            commands = listOf(
                "docker container prune -f",
                "docker image prune -f"
            ),
            icon = "red"
        ),

        // Git Templates
        Template(
            name = "Git Status",
            description = "Show working tree status",
            commands = listOf("git status"),
            icon = "default"
        ),
        Template(
            name = "Git Commit All",
            description = "Stage and commit all changes",
            commands = listOf(
                "git add .",
                "git commit -m \"Update: {date}\""
            ),
            icon = "green"
        ),
        Template(
            name = "Git Pull",
            description = "Fetch and merge from remote",
            commands = listOf("git pull origin main"),
            icon = "blue"
        ),
        Template(
            name = "Git Push",
            description = "Push to remote",
            commands = listOf("git push origin main"),
            icon = "green"
        ),
        Template(
            name = "Git New Branch",
            description = "Create and checkout new branch",
            commands = listOf(
                "git checkout -b feature/{date}",
                "git push -u origin feature/{date}"
            ),
            icon = "purple"
        ),

        // Yarn Templates
        Template(
            name = "Yarn Install",
            description = "Install dependencies with Yarn",
            commands = listOf("yarn install"),
            icon = "blue"
        ),
        Template(
            name = "Yarn Dev",
            description = "Start Yarn development server",
            commands = listOf("yarn dev"),
            icon = "green"
        ),
        Template(
            name = "Yarn Build",
            description = "Build with Yarn",
            commands = listOf("yarn build"),
            icon = "yellow"
        ),

        // Python Templates
        Template(
            name = "Python Virtual Env",
            description = "Create and activate virtual environment",
            commands = listOf(
                "python -m venv venv",
                "source venv/bin/activate"
            ),
            icon = "green"
        ),
        Template(
            name = "Pip Install Requirements",
            description = "Install Python dependencies",
            commands = listOf("pip install -r requirements.txt"),
            icon = "blue"
        ),
        Template(
            name = "Python Run",
            description = "Run Python script",
            commands = listOf("python main.py"),
            icon = "green"
        ),

        // Gradle Templates
        Template(
            name = "Gradle Build",
            description = "Build project with Gradle",
            commands = listOf("./gradlew build"),
            icon = "green"
        ),
        Template(
            name = "Gradle Clean Build",
            description = "Clean and build",
            commands = listOf("./gradlew clean build"),
            icon = "yellow"
        ),
        Template(
            name = "Gradle Test",
            description = "Run tests",
            commands = listOf("./gradlew test"),
            icon = "purple"
        ),

        // Maven Templates
        Template(
            name = "Maven Clean Install",
            description = "Clean and install with Maven",
            commands = listOf("mvn clean install"),
            icon = "red"
        ),
        Template(
            name = "Maven Test",
            description = "Run Maven tests",
            commands = listOf("mvn test"),
            icon = "purple"
        ),

        // Laravel/PHP Templates
        Template(
            name = "Laravel Serve",
            description = "Start Laravel development server",
            commands = listOf("php artisan serve"),
            icon = "red"
        ),
        Template(
            name = "Laravel Migrate",
            description = "Run database migrations",
            commands = listOf("php artisan migrate"),
            icon = "blue"
        ),
        Template(
            name = "Composer Install",
            description = "Install PHP dependencies",
            commands = listOf("composer install"),
            icon = "green"
        ),

        // React Native Templates
        Template(
            name = "React Native Start",
            description = "Start React Native metro bundler",
            commands = listOf("npx react-native start"),
            icon = "blue"
        ),
        Template(
            name = "React Native Android",
            description = "Run on Android",
            commands = listOf("npx react-native run-android"),
            icon = "green"
        ),
        Template(
            name = "React Native iOS",
            description = "Run on iOS",
            commands = listOf("npx react-native run-ios"),
            icon = "default"
        )
    )

    fun getTemplatesByCategory(): Map<String, List<Template>> {
        return mapOf(
            "NPM" to templates.filter { it.name.startsWith("NPM") },
            "Docker" to templates.filter { it.name.startsWith("Docker") },
            "Git" to templates.filter { it.name.startsWith("Git") },
            "Yarn" to templates.filter { it.name.startsWith("Yarn") },
            "Python" to templates.filter { it.name.startsWith("Python") || it.name.startsWith("Pip") },
            "Gradle" to templates.filter { it.name.startsWith("Gradle") },
            "Maven" to templates.filter { it.name.startsWith("Maven") },
            "PHP/Laravel" to templates.filter { it.name.startsWith("Laravel") || it.name.startsWith("Composer") },
            "React Native" to templates.filter { it.name.startsWith("React Native") }
        )
    }
}
