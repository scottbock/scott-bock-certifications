package standup

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
        "/api/$date?/status/$id?"(controller: "statusReportRest") {
            action = [GET: "show", PUT: "update", POST: "save"]
        }

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
