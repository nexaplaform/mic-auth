package com.nexaplatform.api.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class GlobalErrorResponsesCustomizer implements OpenApiCustomizer {

    public static final String NOT_FOUND = "Not Found";
    public static final String FORBIDDEN = "Forbidden";
    public static final String BAD_REQUEST = "Bad Request";
    public static final String CONFLICT = "Conflict";
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    public static final String CONTENT_TYPE = "application/json";
    public static final String CODE_401 = "401";
    public static final String CODE_500 = "500";
    public static final String CONDE_404 = "404";
    public static final String CODE_403 = "403";
    public static final String CODE_400 = "400";
    public static final String CODE_409 = "409";

    @Override
    public void customise(OpenAPI openApi) {

        openApi.getPaths().forEach((pathName, pathItem) -> {
            pathItem.readOperationsMap().forEach((httpMethod, operation) -> {
                ApiResponses apiResponses = operation.getResponses();
                if (apiResponses == null) {
                    apiResponses = new ApiResponses();
                    operation.setResponses(apiResponses);
                }

                addApiResponseIfNotPresent(apiResponses, CODE_401, UNAUTHORIZED);
                addApiResponseIfNotPresent(apiResponses, CODE_500, INTERNAL_SERVER_ERROR);

                switch (httpMethod) {
                    case GET:
                        addApiResponseIfNotPresent(apiResponses, CONDE_404, NOT_FOUND);
                        addApiResponseIfNotPresent(apiResponses, CODE_403, FORBIDDEN);
                        break;
                    case POST:
                        addApiResponseIfNotPresent(apiResponses, CODE_400, BAD_REQUEST);
                        addApiResponseIfNotPresent(apiResponses, CODE_409, CONFLICT);
                        addApiResponseIfNotPresent(apiResponses, CODE_403, FORBIDDEN);
                        break;
                    case PUT:
                    case PATCH:
                        addApiResponseIfNotPresent(apiResponses, CODE_400, BAD_REQUEST);
                        addApiResponseIfNotPresent(apiResponses, CONDE_404, NOT_FOUND);
                        addApiResponseIfNotPresent(apiResponses, CODE_403, FORBIDDEN);
                        break;
                    case DELETE:
                        addApiResponseIfNotPresent(apiResponses, CONDE_404, NOT_FOUND);
                        addApiResponseIfNotPresent(apiResponses, CODE_403, FORBIDDEN);
                        break;
                    default:
                        break;
                }
            });
        });
    }

    private void addApiResponseIfNotPresent(ApiResponses apiResponses, String code, String description) {
        if (!apiResponses.containsKey(code)) {
            ApiResponse apiResponse = new ApiResponse().description(description);

            MediaType mediaType = new MediaType()
                    .schema(createInlineSchema());
            Content content = new Content().addMediaType(CONTENT_TYPE, mediaType);

            apiResponse.content(content);
            apiResponses.addApiResponse(code, apiResponse);
        }
    }

    @SuppressWarnings("unchecked")
    private Schema<?> createInlineSchema() {
        Schema<?> schema = new Schema<>();
        schema.setType("object");

        Map<String, Schema<?>> properties = new LinkedHashMap<>();
        properties.put("code", new StringSchema().example("E001"));
        properties.put("message", new StringSchema().example("Message error description."));

        Schema<String> detailItemSchema = new StringSchema();
        Schema<?> detailsSchema = new Schema<>();
        detailsSchema.setType("array");
        detailsSchema.setItems(detailItemSchema);
        properties.put("details", detailsSchema);

        Schema<String> timestampSchema = new StringSchema();
        timestampSchema.setFormat("date-time");
        timestampSchema.setExample("2024-05-01T20:35:10Z");
        properties.put("timeStamp", timestampSchema);

        schema.setProperties((Map) properties);
        return schema;
    }
}
