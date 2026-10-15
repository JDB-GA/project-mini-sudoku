# Sudoku

A Spring Boot API for uploading sudoku puzzles as CSV files and downloading them by ID.

## Run

```bash
./mvnw spring-boot:run
```

The API runs on `http://localhost:8080`.

## File format

A puzzle is a CSV file with 9 lines of 9 comma-separated numbers. Use `0` for empty cells and `1`–`9` for filled ones. The file has no header line.

```csv
5,3,0,0,7,0,0,0,0
6,0,0,1,9,5,0,0,0
0,9,8,0,0,0,0,6,0
8,0,0,0,6,0,0,0,3
4,0,0,8,0,3,0,0,1
7,0,0,0,2,0,0,0,6
0,6,0,0,0,0,2,8,0
0,0,0,4,1,9,0,0,5
0,0,0,0,8,0,0,7,9
```

Uploads are limited to 10KB.

## Storage

Files are saved in the `data/` folder at the project root. You can change the folder with `sudoku.data-dir` in `application.properties`. Each puzzle gets a UUID, and its files are named after it:

| File | Content |
|---|---|
| `{id}-q.csv` | the uploaded question |
| `{id}-a.csv` | the solved answer |

## Endpoints

### 1. Upload a puzzle

`POST /sudoku`

Uploads a puzzle file, validates it, saves it as `{id}-q.csv` and returns the new ID.

```bash
curl -F "file=@puzzle.csv" localhost:8080/sudoku
```

Response `201 Created`:

```json
{ "id": "c6ff7e74-b642-452d-aa5b-c6ed51fcfe00" }
```

### 2. Download a question

`GET /sudoku/{id}/question`

Downloads the uploaded puzzle as `{id}-q.csv`.

```bash
curl -OJ localhost:8080/sudoku/c6ff7e74-b642-452d-aa5b-c6ed51fcfe00/question
```

Response `200 OK` with the CSV file.

### 3. Download an answer

`GET /sudoku/{id}/answer`

Downloads the solved puzzle as `{id}-a.csv`. It returns `404` until the puzzle has been solved.

```bash
curl -OJ localhost:8080/sudoku/c6ff7e74-b642-452d-aa5b-c6ed51fcfe00/answer
```

Response `200 OK` with the CSV file.

## Errors

Errors are returned as plain text.

| Status | Message | When |
|---|---|---|
| `400` | Sudoku file must have 9 lines with 9 numbers from 0 to 9 | The upload is empty, has the wrong size or contains a value that isn't 0–9 |
| `400` | Invalid sudoku id | The ID isn't a valid UUID |
| `404` | Sudoku not found | No file exists for that ID |
| `413` | File is too large | The upload is over 10KB |
| `500` | An error occurred while reading lines / writing lines | The server failed to read or write a file |
| `500` | Something went wrong | Any other unexpected error |
